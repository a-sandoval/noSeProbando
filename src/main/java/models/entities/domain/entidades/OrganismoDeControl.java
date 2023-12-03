package models.entities.domain.entidades;

import models.entities.domain.Persistente;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="organismo_de_control")
public class OrganismoDeControl extends Persistente {

    @Column(name="nombre")
    private String nombre;
    @Column(name="cuit")
    private String CUIT;
    @OneToMany(mappedBy = "organismoDeControl", cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<PrestadoraDeServicio> entidadesPrestadorasControladas;

    public OrganismoDeControl() {
        this.entidadesPrestadorasControladas = new ArrayList<>();
    }

    public void aniadirPrestadoraControlada(PrestadoraDeServicio prestadora) {
        this.entidadesPrestadorasControladas.add(prestadora);
        prestadora.setOrganismoDeControl(this);
    }

}
