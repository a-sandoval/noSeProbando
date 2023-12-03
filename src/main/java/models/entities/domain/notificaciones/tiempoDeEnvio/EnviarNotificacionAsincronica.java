package models.entities.domain.notificaciones.tiempoDeEnvio;

import models.entities.domain.notificaciones.Notificacion;
import models.entities.domain.notificaciones.Notificador;
import models.entities.domain.registro.Usuario;
import lombok.Getter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import javax.mail.MessagingException;
import java.util.ArrayList;

@Getter

public class EnviarNotificacionAsincronica implements Job {
    private ArrayList<Notificacion> notificacionesSinEnviar;
    private final Usuario usuario;

    public EnviarNotificacionAsincronica(Usuario usuario) {

        notificacionesSinEnviar = new ArrayList<>();

        this.usuario = usuario;
    }

    public void agregarNotificacionSinEnviar(Notificacion notificacion) {

        notificacionesSinEnviar.add(notificacion);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        Notificador notificador = Notificador.getInstancia();

        try {
            notificador.enviarNotificacionResumenA(usuario, this.notificacionesSinEnviar);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
