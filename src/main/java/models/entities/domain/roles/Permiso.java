package models.entities.domain.roles;

import models.entities.domain.Persistente;
import lombok.Getter;
import lombok.Setter;
import models.entities.domain.entidades.OrganismoDeControl;
import models.entities.domain.entidades.PrestadoraDeServicio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "permiso")
public class Permiso extends Persistente {

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "nombreInterno")
    private String nombreInterno;

    @Column(name="descripcion")
    private String descripcion;

    @OneToOne
    private PrestadoraDeServicio prestadoraDeServicio;

    @OneToOne
    private OrganismoDeControl organismoDeControl;

    public boolean coincideConNombreInterno(String nombre) {
        return this.nombreInterno.equals(nombre);
    }


}
