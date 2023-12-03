package controllers;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import models.entities.domain.registro.Usuario;

public abstract class Controller implements WithSimplePersistenceUnit {

    protected Usuario usuarioLogueado(Context ctx) {
        if (ctx.sessionAttribute("id") == null)
            return null;
        return entityManager()
                .find(Usuario.class, Long.parseLong(ctx.sessionAttribute("id").toString()));
    }
}
