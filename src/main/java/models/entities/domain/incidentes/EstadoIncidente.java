package models.entities.domain.incidentes;

import models.entities.domain.Persistente;
import models.entities.domain.registro.Usuario;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@Setter
@Table(name="estado_incidente")
@Entity
//Esto se tiene que persistir para tener trazabilidad en los estados de los incidentes
public class EstadoIncidente extends Persistente {
    @OneToOne(fetch = FetchType.LAZY)
    private Usuario usuarioResponsable;
    @Column(name="fecha_modificacion")
    private LocalDateTime fechaModificacion;
    @Enumerated(EnumType.STRING)
    private Estado estado;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="incidente_id", referencedColumnName = "id")
    private Incidente incidente;

    public EstadoIncidente() {

        this.estado = Estado.ACTIVO;
    }

    public EstadoIncidente(Usuario usuarioResponsable, LocalDateTime fechaModificacion) {
        this.usuarioResponsable = usuarioResponsable;
        this.fechaModificacion = fechaModificacion;
        this.estado = Estado.ACTIVO;
        this.incidente = incidente;

    }
}