package tests.domain.notificaciones;

import models.entities.domain.comunidad.Comunidad;
import models.entities.domain.entidades.Establecimiento;
import models.entities.domain.incidentes.Estado;
import models.entities.domain.incidentes.Incidente;
import models.entities.domain.incidentes.ReportadorDeIncidentes;
import models.entities.domain.notificaciones.medioEnvio.WhatsApp;
import models.entities.domain.notificaciones.tiempoDeEnvio.ModoRecepcion;
import models.entities.domain.notificaciones.tiempoDeEnvio.PreferenciaEnvioNotificacion;
//import domain.notificaciones.tiempoDeEnvio.EnviarNotificacionAsincronica;
import models.entities.domain.registro.Contrasenia;
import models.entities.domain.registro.Registro;
import models.entities.domain.registro.Usuario;
import models.entities.domain.services.georef.entities.Ubicacion;
import models.entities.domain.servicios.PrestacionDeServicio;
import models.entities.domain.servicios.Servicio;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;

public class NotificacionesTest {

    Contrasenia contrasenia = new Contrasenia("Juan1234");

    PreferenciaEnvioNotificacion preferencia = new PreferenciaEnvioNotificacion(new WhatsApp(), ModoRecepcion.SINCRONICA);

    Usuario usuarioJuan = new Usuario("juan", "jperez@gmail.com", contrasenia);

    ReportadorDeIncidentes reportadorDeIncidentes = new ReportadorDeIncidentes();

    Servicio banios = new Servicio("Baño", "Mujeres");

    Establecimiento macdonalds = new Establecimiento("McDonalds", new Ubicacion(-34.5602699, -58.458387));

    PrestacionDeServicio servicioAfectado = new PrestacionDeServicio(banios, macdonalds);

    @Test
    public void seCreaUnIncidenteTest() throws MessagingException {

        Registro.getInstancia().registrarUsuario(usuarioJuan);

        Comunidad comunidadAfectada = new Comunidad();

        Incidente incidente = reportadorDeIncidentes.crearIncidente(servicioAfectado, usuarioJuan, comunidadAfectada, "");

        assert (incidente.getEstado().getEstado().equals(Estado.ACTIVO));
    }

}



