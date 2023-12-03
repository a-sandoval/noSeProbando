package models.entities.domain.roles;

import models.entities.domain.Persistente;
import models.entities.domain.comunidad.Comunidad;
import lombok.Getter;
import lombok.Setter;
import models.entities.domain.entidades.OrganismoDeControl;
import models.entities.domain.entidades.PrestadoraDeServicio;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "rol")
public class Rol extends Persistente {

    @Column(name="nombre")
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoRol tipo;



    @OneToOne
    @Nullable
    private Comunidad comunidad;

    @ManyToMany
    private List<Permiso> permisos;

    boolean tenesPermiso(Permiso unPermiso) {

        return permisos.contains(unPermiso);

    }

    public void agregarPermisos(Permiso ... permisos) {
        Collections.addAll(this.permisos, permisos);
    }

    public boolean tenesPermiso(String nombreInterno) {
        return this.permisos.stream().anyMatch(p -> p.coincideConNombreInterno(nombreInterno));
    }

    public Rol() {

        permisos = new ArrayList<>();
    }
}
