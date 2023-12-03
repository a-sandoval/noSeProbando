package controllers;

import models.entities.domain.comunidad.Comunidad;
import models.entities.domain.entidades.OrganismoDeControl;
import models.entities.domain.entidades.PrestadoraDeServicio;
import models.entities.domain.registro.Usuario;
import models.entities.domain.roles.Permiso;
import models.entities.domain.roles.Rol;
import models.entities.domain.roles.TipoRol;
import models.repositories.datos.RepositorioDePermisos;
import models.repositories.datos.RepositorioDeRoles;
import models.repositories.datos.RepositorioUsuarios;

import java.util.List;

public class RolPermisoController {

    private RepositorioDePermisos repositorioDePermisos;
    private RepositorioUsuarios repositorioUsuarios;
    private RepositorioDeRoles repositorioDeRoles;

    public RolPermisoController(RepositorioDePermisos repositorioDePermisos, RepositorioUsuarios repositorioUsuarios, RepositorioDeRoles repositorioDeRoles) {
        this.repositorioDePermisos = repositorioDePermisos;
        this.repositorioUsuarios = repositorioUsuarios;
        this.repositorioDeRoles  = repositorioDeRoles;
    }

    //Esto se hace al crearse una comunidad
    public Rol asignarPermisosComoMiembroDeComunidad(Comunidad comunidad) {

        Rol miembro = new Rol();

        miembro.setComunidad(comunidad);

        miembro.setTipo(TipoRol.NORMAL);

        miembro.setNombre("Miembro de " + comunidad.getNombre());

        this.asignarPermisosDeMiembro(miembro);
        return miembro;
    }


    public void asignarPermisosDeMiembro(Rol rol) {
        rol.agregarPermisos(repositorioDePermisos.buscarPermisoPorNombre("reportar_incidentes"), repositorioDePermisos.buscarPermisoPorNombre("cerrar_incidentes"), repositorioDePermisos.buscarPermisoPorNombre("ver_incidentes"));

    }

    public Rol asignarPermisosComoAdminDeComunidad(Comunidad comunidad) {
        Rol admin = new Rol();

        admin.setComunidad(comunidad);

        admin.setTipo(TipoRol.ADMINISTRADOR);

        admin.setNombre("Administrador de " + comunidad.getNombre());

        this.asignarPermisosDeMiembro(admin);

        admin.agregarPermisos(repositorioDePermisos.buscarPermisoPorNombre("editar_comunidad"), repositorioDePermisos.buscarPermisoPorNombre("ver_participantes"), repositorioDePermisos.buscarPermisoPorNombre("controlar_miembros"), repositorioDePermisos.buscarPermisoPorNombre("registrar_servicios"));

        return admin;
    }

    public void asignarRolDeMiembroEn(Usuario usuario, Comunidad comunidad) {

        List<Rol> roles = repositorioDeRoles.buscarRolesPorComunidad(comunidad);

        for(Rol rol: roles) {
            if(rol.getTipo().equals(TipoRol.NORMAL)) {
                usuario.aniadirRol(rol);
                this.repositorioUsuarios.actualizar(usuario);
                break;
            }
        }

    }

    public void asignarPermisosDeDesignado(Usuario usuario, PrestadoraDeServicio prestadoraDeServicio) {

        //Persona designada por Prestadora de Servicio
        String[][] permisos = { {"Visualizar rankings de entidades", "ver_rankings_entidades"}};

        for(String[] unPermiso: permisos) {
            Permiso permiso = new Permiso();
            permiso.setNombre(unPermiso[0]);
            permiso.setNombreInterno(unPermiso[1]);
            permiso.setPrestadoraDeServicio(prestadoraDeServicio);
            usuario.setPermisoEspecialDeDesignado(permiso);
            repositorioDePermisos.guardar(permiso);
        }

    repositorioUsuarios.actualizar(usuario);
}

    public void asignarPermisosDeDesignado(Usuario usuario, OrganismoDeControl organismoDeControl) {

        String[][] permisos = { {"Visualizar rankings de prestadoras de servicio", "ver_rankings_prestadoras"}};

        for(String[] unPermiso: permisos) {
            Permiso permiso = new Permiso();
            permiso.setNombre(unPermiso[0]);
            permiso.setNombreInterno(unPermiso[1]);
            permiso.setOrganismoDeControl(organismoDeControl);
            usuario.setPermisoEspecialDeDesignado(permiso);
            repositorioDePermisos.guardar(permiso);
        }

        repositorioUsuarios.actualizar(usuario);
    }
}
