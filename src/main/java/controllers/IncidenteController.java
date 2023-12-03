package controllers;


import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import models.entities.domain.comunidad.Comunidad;
import models.entities.domain.entidades.Establecimiento;
import models.entities.domain.incidentes.Estado;
import models.entities.domain.incidentes.EstadoIncidente;
import models.entities.domain.registro.Usuario;
import models.entities.domain.services.georef.entities.Ubicacion;
import models.entities.domain.servicios.PrestacionDeServicio;
import models.repositories.datos.*;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import server.exceptions.AccessDeniedException;
import server.utils.ICrudViewsHandler;
import models.entities.domain.incidentes.Incidente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class IncidenteController extends Controller implements ICrudViewsHandler {
    private RepositorioIncidentes repositorioIncidentes;
    private RepositorioPrestacionesDeServicio repositorioPrestacionesDeServicio;
    private RepositorioComunidades repositorioComunidades;

    private RepositorioUsuarios repositorioUsuarios;
    public IncidenteController(RepositorioIncidentes repositorioDeIncidentes, RepositorioPrestacionesDeServicio repositorioPrestacionesDeServicio, RepositorioComunidades repositorioComunidades, RepositorioUsuarios repositorioUsuarios) {
        this.repositorioIncidentes = repositorioDeIncidentes;
        this.repositorioPrestacionesDeServicio = repositorioPrestacionesDeServicio;
        this.repositorioComunidades = repositorioComunidades;
        this.repositorioUsuarios = repositorioUsuarios;

    }


    @Override
    public void index(Context context) {
        Map<String, Object> model = new HashMap<>();
        String id = Objects.requireNonNull(context.sessionAttribute("id")).toString();
        Usuario usuario = (Usuario) this.repositorioUsuarios.buscar(Long.parseLong(id));
        List<Incidente> incidentes = this.repositorioIncidentes.buscarIncidentesDeInteresPara(Long.parseLong(id));
        model.put("incidentes", incidentes);

        //Aca tengo que buscar los permisos del usuario, tengo que saber: 1. si puede leer CSV
        //2. si puede ver los rankings
        Boolean permisoCSV = usuario.tenesPermiso("cargar_csv");
        Boolean permisoRanking = usuario.tenesPermiso("ver_rankings_entidades");

        model.put("permisoCSV", permisoCSV);
        model.put("permisoRanking", permisoRanking);

        //context.result("Hola");
        context.render("presentacion/menuPrincipal.hbs", model);
    }

    //el show no lo implementamos pq ya con el index mostramos los detalles del incidente.
    @Override
    public void show(Context context) {

    }

    @Override
    public void create(Context context) {
        Map<String, Object> model = new HashMap<>();
        String id = context.sessionAttribute("id").toString();
        List<Long> idComunidades = this.repositorioComunidades.buscarComunidadesDe(Long.parseLong(id));
        List<Comunidad> comunidades = new ArrayList<>();
        List<PrestacionDeServicio> prestacionesDeServicios = new ArrayList();

        for(Long idComunidad: idComunidades) {
            comunidades.add(this.repositorioComunidades.obtenerComunidad(idComunidad));
        }

        String referrer = context.header("Referer");
        if (referrer != null && referrer.contains("/comunidades/")) {
            String comunidadIdFromReferrer = referrer.substring(referrer.lastIndexOf("/") + 1);
            Long idComunidadSeleccionada = Long.parseLong(comunidadIdFromReferrer);

            Comunidad comunidad = this.repositorioComunidades.obtenerComunidad(idComunidadSeleccionada);

            prestacionesDeServicios = comunidad.getServiciosDeInteres();

            if (comunidad != null) {
                model.put("comunidadSeleccionada", comunidad);
                prestacionesDeServicios = comunidad.getServiciosDeInteres();
            }
        }

        model.put("comunidades", comunidades);
        model.put("servicios", prestacionesDeServicios);

        context.render("incidentes/aperturaIncidentes.hbs", model);
    }

    @Override
    public void save(Context context) {
        Usuario usuarioLogueado =  super.usuarioLogueado(context);
        PrestacionDeServicio servicioAfectado = null;
        Comunidad comunidad = null;
        String descripcion = null;

        if(usuarioLogueado == null || !usuarioLogueado.tenesPermiso("reportar_incidentes")) {
            throw new AccessDeniedException();
        }

        if (context.formParam("servicio") != null) {
            Long servicioAfectadoId = Long.parseLong(context.formParam("servicio"));
            servicioAfectado = (PrestacionDeServicio) this.repositorioPrestacionesDeServicio.buscar(servicioAfectadoId);
        }

        if (context.formParam("comunidad") != null) {
            Long comunidadId = (Long) this.repositorioComunidades.obtenerIdDeComunidadPorNombre(context.formParam("comunidad"));
            comunidad = this.repositorioComunidades.obtenerComunidad(comunidadId);
        }

        if (context.formParam("observaciones") != null) { // creo que esto por ser na string lo vamos a tener que cambiar, eze lo hizo con un object equals
            descripcion = context.formParam("observaciones");
        }

        Incidente incidente = new Incidente(servicioAfectado, usuarioLogueado, comunidad, descripcion);


        //this.asignarParametros(incidente, context); lo vole porque el contructor de incidente tenia mas complejidad kjj
        this.repositorioIncidentes.guardar(incidente);
        EstadoIncidente estado = new EstadoIncidente(usuarioLogueado, LocalDateTime.now());
        new RepositorioEstadoIncidente().guardar(estado);
        incidente.getEstadosDeIncidente().add(estado);
        this.repositorioIncidentes.actualizar(incidente);
        estado.setIncidente(incidente);
        new RepositorioEstadoIncidente().actualizar(estado);
        context.status(HttpStatus.CREATED);
        context.redirect("/home");
    }
    @Override
    public void edit(Context context) {

        Incidente incidente  = (Incidente) this.repositorioIncidentes.buscar(Long.parseLong(context.pathParam("id")));
        Map<String, Object> model = new HashMap<>();
        PrestacionDeServicio servicioAfectado = incidente.getServicioAfectado();
        Establecimiento establecimiento = servicioAfectado.getEstablecimiento();
        String estado = incidente.getEstado().getEstado().toString();
        String descripcion =  incidente.getDescripcion();
        model.put("incidente", incidente);
        model.put("establecimiento", establecimiento);
        model.put("servicioAfectado", servicioAfectado);
        model.put("estado", estado);
        model.put("descripcion", descripcion);
        context.render("incidentes/cierreIncidentes.hbs", model);

    }

    @Override
    public void update(Context context) {

        Incidente incidente  = (Incidente) this.repositorioIncidentes.buscar(Long.parseLong(context.pathParam("id")));
        Usuario usuarioLogueado =  super.usuarioLogueado(context);

        if(usuarioLogueado == null || !usuarioLogueado.tenesPermiso("cerrar_incidentes")) {
            throw new AccessDeniedException();
        }

        if(context.formParam("resolucion").toString().equals("si")) {


            LocalDateTime fechaResolucion = LocalDateTime.now();
            EstadoIncidente nuevoEstado = new EstadoIncidente(usuarioLogueado, fechaResolucion);
            nuevoEstado.setIncidente(incidente);
            nuevoEstado.setEstado(Estado.RESUELTO);

            incidente.setEstado(nuevoEstado);
            incidente.setFechaResolucion(fechaResolucion);

            repositorioIncidentes.actualizar(incidente);

        }

        context.redirect("/home");

    }

    @Override
    public void delete(Context context) {

    }

    public void suggest(Context context){

        Usuario usuarioLogueado =  super.usuarioLogueado(context);

        if(usuarioLogueado == null) {
            throw new AccessDeniedException();
        }

        List<Incidente> incidentes;
        if(usuarioLogueado.getLocalizacion() != null) {
           incidentes = this.repositorioIncidentes.filtrarPorUbicacionCercana(usuarioLogueado.getLocalizacion());
        }

        else
           incidentes = null;

        Map<String, Object> model = new HashMap<>();
        model.put("incidentes", incidentes);

        context.render("incidentes/sugerencias.hbs", model);

    }

    public void api(Context context){
        Usuario usuarioLogueado = super.usuarioLogueado(context);
        List<Map<String, Object>> incidentesData = new ArrayList<>();

        List<Incidente> incidentes = this.repositorioIncidentes.buscarIncidentesDeInteresPara(usuarioLogueado.getId());

        for (Incidente incidente : incidentes) {
            PrestacionDeServicio servicioAfectado = incidente.getServicioAfectado();
            Establecimiento establecimiento = servicioAfectado.getEstablecimiento();
            Ubicacion ubicacion = establecimiento.getUbicacionGeografica();

            Map<String, Object> incidenteData = new HashMap<>();
            incidenteData.put("latitud", ubicacion.getLat());
            incidenteData.put("longitud", ubicacion.getLon());
            incidenteData.put("nombreEstablecimiento", establecimiento.getNombre());
            incidenteData.put("nombreServicioAfectado", servicioAfectado.getNombre());
            incidenteData.put("descripcion", incidente.getDescripcion());
            incidenteData.put("id", incidente.getId());

            incidentesData.add(incidenteData);
        }

        context.json(incidentesData);
    }




}
