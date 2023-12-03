package controllers;

import io.javalin.http.Context;
import models.repositories.datos.RepositorioPrestacionesDeServicio;
import server.utils.ICrudViewsHandler;

public class PrestacionDeServicioController implements ICrudViewsHandler {
    RepositorioPrestacionesDeServicio repositorioPrestacionesDeServicio;

    public PrestacionDeServicioController(RepositorioPrestacionesDeServicio repositorioDePrestacionesDeServicio) {
        this.repositorioPrestacionesDeServicio = repositorioDePrestacionesDeServicio;
    }

    @Override
    public void index(Context context) {

    }

    @Override
    public void show(Context context) {

    }

    @Override
    public void create(Context context) {

    }

    @Override
    public void save(Context context) {

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
