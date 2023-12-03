package models.entities.domain.servicios;
import models.entities.domain.Persistente;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name="servicio")
public class Servicio extends Persistente {

        @Column(name="tipoDeServicio")
        private String tipoDeServicio;

        @Column(name="descripcion")
        public String descripcion;

        public Servicio(String tipoDeServicio, String descripcion) {
                this.tipoDeServicio = tipoDeServicio;
                this.descripcion = descripcion;
        }

        public Servicio() {

        }
        public void modificarDescripcion(String nuevaDescripcion){
                this.descripcion = nuevaDescripcion;
        }

}
