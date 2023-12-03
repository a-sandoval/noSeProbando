package controllers;

import io.javalin.http.Context;
import models.repositories.datos.RepositorioOrganismosDeControl;
import server.utils.ICrudViewsHandler;

public class OrganismoDeControlController implements ICrudViewsHandler {
    private RepositorioOrganismosDeControl repositorioOrganismosDeControl;

    public OrganismoDeControlController(RepositorioOrganismosDeControl repositorioDeOrganismosDeControl) {
        this.repositorioOrganismosDeControl = repositorioDeOrganismosDeControl;
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
