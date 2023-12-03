package controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import models.entities.domain.comunidad.Comunidad;
import models.entities.domain.incidentes.Estado;
import models.entities.domain.incidentes.EstadoIncidente;
import models.entities.domain.incidentes.Incidente;
import models.entities.domain.registro.Usuario;
import models.entities.domain.roles.Rol;
import models.entities.domain.servicios.PrestacionDeServicio;
import models.entities.domain.servicios.Servicio;
import models.repositories.datos.*;
import org.jetbrains.annotations.NotNull;
import server.exceptions.AccessDeniedException;
import server.utils.ICrudViewsHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class ComunidadController extends Controller implements ICrudViewsHandler {
    private RepositorioComunidades repositorioComunidades;
    private RepositorioUsuarios repositorioUsuarios;
    private RepositorioPrestacionesDeServicio repositorioPrestacionesDeServicio;
    private RepositorioServicios repositorioServicios;

    public ComunidadController(RepositorioComunidades repositorioComunidades, RepositorioUsuarios repositorioUsuarios, RepositorioPrestacionesDeServicio repositorioPrestacionesDeServicio) {
        this.repositorioComunidades = repositorioComunidades;
        this.repositorioUsuarios = repositorioUsuarios;
        this.repositorioPrestacionesDeServicio = repositorioPrestacionesDeServicio;
    }

    @Override
    public void index(Context context) {
        Map<String, Object> model = new HashMap<>();
        String id = Objects.requireNonNull(context.sessionAttribute("id")).toString();
        Usuario usuario = (Usuario) this.repositorioUsuarios.buscar(Long.parseLong(id));

        Boolean permisoCreacion = usuario.tenesPermiso("crear_comunidades");
        model.put("permisoCreacion", permisoCreacion);

        List<Long> idComunidades = this.repositorioComunidades.buscarComunidadesDe(Long.parseLong(id));

        List<Comunidad> comunidades = new ArrayList<>();

        for(Long idComunidad: idComunidades) {

            comunidades.add(this.repositorioComunidades.obtenerComunidad(idComunidad));
        }

        model.put("comunidades", comunidades);
        context.render("comunidades/comunidades.hbs", model);
    }

    @Override
    public void show(Context context) {
        Map<String, Object> model = new HashMap<>();
        String id = context.sessionAttribute("id").toString();
        List<Comunidad> comunidades = this.repositorioComunidades.buscarComunidadesNoDe(Long.parseLong(id));
        model.put("comunidades", comunidades);
        context.render("comunidades/unirseAComunidad.hbs", model);

    }

    @Override
    public void create(Context context) {
        //Valido unicamente para SuperAdmin de la plataforma

        Usuario usuarioLogueado =  super.usuarioLogueado(context);

        if(usuarioLogueado == null) { //|| !usuarioLogueado.tenesPermiso("crear_comunidades")) {
            throw new AccessDeniedException();
        }
        RepositorioPrestacionesDeServicio repositorio = new RepositorioPrestacionesDeServicio();
        RepositorioEntidades repositorioEntidades  = new RepositorioEntidades();
        Map<String, Object> model = new HashMap<>();
        List<String> entidades = repositorioEntidades.buscarTodosLosNombresDeEntidades();
       List<String> servicios = repositorio.buscarTodosLosNombres();
        model.put("servicios", entidades);
        context.render("comunidades/crearComunidades.hbs", model);
    }

    @Override
    public void save(Context context) {

        Comunidad comunidad = new Comunidad();
        this.asignarParametros(comunidad, context);
        this.repositorioComunidades.guardar(comunidad);
        context.status(HttpStatus.CREATED);
        context.redirect("/comunidades/unirse");
    }

    @Override
    public void edit(Context context) {

    }

    @Override
    public void update(Context context) {

    }

    @Override
    public void delete(Context context) {
        Comunidad comunidad = (Comunidad) this.repositorioComunidades.buscar(Long.parseLong(context.pathParam("id")));
        this.repositorioComunidades.eliminar(comunidad);
        context.redirect("/comunidades");

    }

    public void asignarParametros(Comunidad comunidad, Context context){
        if(!Objects.equals(context.formParam("nombre"), "")) {
            comunidad.setNombre(context.formParam("nombre"));
        }
        if(!Objects.equals(context.formParam("descripcion"), "")) {
            comunidad.setDescripcion(context.formParam("descripcion"));
        }

        List<String> serviciosInteres = context.formParams("servicios");
        List<PrestacionDeServicio> serviciosDeInteres = new ArrayList<>();

        if (serviciosInteres != null) {
            for (String servicioInteresNombre : serviciosInteres) {
                Long servicioInteresId = this.repositorioPrestacionesDeServicio.obtenerIdDelServicioPorNombre(servicioInteresNombre);
                if (servicioInteresId != null) {
                    PrestacionDeServicio servicioInteres = (PrestacionDeServicio) this.repositorioPrestacionesDeServicio.buscar(servicioInteresId);
                    serviciosDeInteres.add(servicioInteres);
                }
            }
        }
        comunidad.setServiciosDeInteres(serviciosDeInteres);

    }

    public void showById(Context context) {

        Comunidad comunidad = this.repositorioComunidades.obtenerComunidad(Long.parseLong(context.pathParam("id").toString()));

        Map<String, Object> model = new HashMap<>();
        List<Incidente> incidentes = RepositorioIncidentes.getInstance().filtrarPorComunidad(comunidad);
        List<Incidente> incidentesOrdenados = ordenarPorEstadoActivo(incidentes);


        model.put("comunidad", comunidad);

        model.put("incidentes", incidentesOrdenados);

        context.render("comunidades/comunidad.hbs", model);

    }
    public List<Incidente> ordenarPorEstadoActivo(List<Incidente> incidentes) {
        return incidentes.stream()
                .sorted(Comparator.comparing(incidente -> {
                    EstadoIncidente estadoIncidente = incidente.getEstado();
                    return estadoIncidente != null && estadoIncidente.getEstado() == Estado.ACTIVO ? 0 : 1;
                }))
                .collect(Collectors.toList());
    }

}



