package models.entities.domain.roles;

import io.javalin.security.RouteRole;

public enum TipoRol implements RouteRole {
    ADMINISTRADOR,
    NORMAL,
    SUPERADMINISTRADOR
}
