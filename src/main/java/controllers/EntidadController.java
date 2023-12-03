package controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import models.entities.domain.entidades.Entidad;
import models.entities.domain.incidentes.Incidente;
import models.repositories.datos.RepositorioEntidades;
import server.utils.ICrudViewsHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntidadController implements ICrudViewsHandler {
    private RepositorioEntidades repositorioEntidades;

    public EntidadController(RepositorioEntidades repositorioDeServicios) {
        this.repositorioEntidades = repositorioDeServicios;
    }

    @Override
    public void index(Context context) {
        Map<String, Object> model = new HashMap<>();
        String id = context.sessionAttribute("id").toString();
        if(id!=null){
            List<String> entidadesDeInteres = this.repositorioEntidades.buscarTodosLosNombresDeEntidadesDeInteres(Long.parseLong(id));
            List<String> entidades = this.repositorioEntidades.buscarTodosLosNombresDeEntidades();
            List<String> entidadesUnicas = new ArrayList<>(entidades);
            entidadesUnicas.removeAll(entidadesDeInteres);
            model.put("entidades", entidadesUnicas);
        }
        else{
            List<String> entidades = this.repositorioEntidades.buscarTodosLosNombresDeEntidades();
            model.put("entidades", entidades);
        }


        context.render("index/registroEntidadesDeInteres.hbs", model);
    }

    @Override
    public void show(Context context) {

    }

    @Override
    public void create(Context context) {

    }

    @Override
    public void save(Context context) {
        Entidad entidad = new Entidad();
        String nombre = "";
        entidad.setNombre(nombre);
        repositorioEntidades.guardar(entidad);
        context.status(HttpStatus.CREATED);
    }

    @Override
    public void edit(Context context) {

    }

    @Override
    public void update(Context context) {

    }

    @Override
    public void delete(Context context) {

    }
}
