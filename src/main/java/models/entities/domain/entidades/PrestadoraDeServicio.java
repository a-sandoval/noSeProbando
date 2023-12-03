package models.entities.domain.entidades;
import models.entities.domain.Persistente;
import models.entities.domain.registro.Usuario;
import lombok.Getter;
import lombok.Setter;
import models.repositories.datos.RepositorioDePermisos;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="prestadora_de_servicio")
public class PrestadoraDeServicio extends Persistente {
    @Column(name="nombre")
    private String nombre;
    @OneToMany(mappedBy="prestadora", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Entidad> entidades;
    @OneToOne(fetch = FetchType.LAZY)
    private Usuario usuarioDesignado;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="organismoDeControl_id", referencedColumnName = "id")
    private OrganismoDeControl organismoDeControl;

    public PrestadoraDeServicio(){ this.entidades = new ArrayList<>(); }

    public PrestadoraDeServicio(String nombre) {

        this.entidades = new ArrayList<>();
        this.setNombre(nombre);
    }
    public void aniadirEntidad(Entidad entidad){
        entidades.add(entidad);
        entidad.setPrestadora(this);
    }

    public void asignarUsuario(Usuario usuario) {

        this.usuarioDesignado = usuario;

        usuarioDesignado.setPermisoEspecialDeDesignado(new RepositorioDePermisos().buscarPermisoPorNombre("ver_rankings_entidades"));
    }


}