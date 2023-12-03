package models.entities.domain.incidentes;

import models.entities.domain.comunidad.Comunidad;
import models.entities.domain.notificaciones.Notificador;
import models.entities.domain.registro.Usuario;
import models.entities.domain.servicios.PrestacionDeServicio;
import lombok.Getter;
import lombok.Setter;

import javax.mail.MessagingException;

@Getter
@Setter
public class ReportadorDeIncidentes {
    private static ReportadorDeIncidentes instancia = null;
    public static ReportadorDeIncidentes getInstancia(){
        if(instancia==null) {
            instancia = new ReportadorDeIncidentes();
        }
        return instancia;
    }

    Notificador notificador = Notificador.getInstancia();

    public Incidente crearIncidente(PrestacionDeServicio prestacionDeServicio, Usuario usuario, Comunidad comunidad, String observacion) throws MessagingException {

        Incidente nuevoIncidente = new Incidente();

        nuevoIncidente.setEstado(new EstadoIncidente());

        nuevoIncidente.setServicioAfectado(prestacionDeServicio);

        nuevoIncidente.setUsuarioReportador(usuario);

        nuevoIncidente.setComunidadDondeSeReporta(comunidad);

        nuevoIncidente.setDescripcion(observacion);

        notificador.creeUnIncidente(nuevoIncidente);

        comunidad.seInformoUnIncidente(nuevoIncidente);

        return nuevoIncidente;
    }
}

