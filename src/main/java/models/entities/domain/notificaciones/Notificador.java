package models.entities.domain.notificaciones;

import models.repositories.datos.RepositorioIncidentes;

import models.entities.domain.config.Config;
import models.entities.domain.notificaciones.creacion.ContextoDeIncidente;
import models.entities.domain.notificaciones.tiempoDeEnvio.ValidadorNotificacionAsincronica;
import models.entities.domain.registro.Usuario;
import models.entities.domain.incidentes.Incidente;
import models.entities.domain.services.georef.entities.Ubicacion;
import lombok.Getter;
import lombok.Setter;
import models.entities.domain.notificaciones.creacion.NotificacionBuilder;
import org.json.JSONObject;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Notificador {

    private static Notificador instancia = null;
    private static NotificacionBuilder builder;

    public static Notificador getInstancia() {
        if (instancia == null) {
            instancia = new Notificador();
            builder = new NotificacionBuilder();
        }

        return instancia;
    }

    public void creeUnIncidente(Incidente incidente) throws MessagingException {

        builder.construirTexto(incidente, ContextoDeIncidente.CREACION);
        this.notificar(incidente);
    }

    public void cerreUnIncidente(Incidente incidente) throws MessagingException {

        builder.construirTexto(incidente, ContextoDeIncidente.CIERRE);
        this.notificar(incidente);
    }

    public void pedidoDeRevisionDeIncidente(Incidente incidente) throws MessagingException {

        builder.construirTexto(incidente, ContextoDeIncidente.SUGERENCIA_DE_REVISION);
        this.notificar(incidente);
    }

    public void enviarSugerenciasRevisionA(Usuario usuario) throws MessagingException {

        Ubicacion localizacionUsuario = usuario.getLocalizacion();
        List<Incidente> incidentesCercanos;
        incidentesCercanos = (List<Incidente>) RepositorioIncidentes.getInstance().filtrarPorUbicacionCercana(usuario.getLocalizacion());

        for (Incidente incidenteCercano : incidentesCercanos) {
            this.pedidoDeRevisionDeIncidente(incidenteCercano);
        }
    }

    public void notificar(Incidente incidente) throws MessagingException {

        ArrayList<Usuario> usuariosInteresados = incidente.obtenerUsuariosInteresados();
        this.notificarUsuarios(usuariosInteresados, builder.construirNotificacion(LocalDateTime.now()));
    }

    public void notificarUsuarios(ArrayList<Usuario> usuarios, Notificacion notificacion) throws MessagingException {

        int i;
        if (!usuarios.isEmpty()) {
            for (i = 0; i < usuarios.size(); i++) {

                Usuario usuario = usuarios.get(i);

                notificacion.enviarseA(usuario);
            }
        }

    }

    public void notificar(Usuario usuario) throws MessagingException {

        Notificacion notificacion = builder.construirNotificacion(LocalDateTime.now());
        notificacion.enviarseA(usuario);

    }

    public void enviarNotificacionResumenA(Usuario usuario, ArrayList<Notificacion> notificacionesSinEnviar) throws MessagingException {

        String rutaArchivo = Config.RUTA_ARCHIVOS + "textos_incidente.json";

        try {
            FileReader archivo = new FileReader(rutaArchivo);

            JSONObject json = new JSONObject(archivo);

            builder.construirTexto(json.getString("ResumenDeIncidentes"));

            for (Notificacion notificacion : notificacionesSinEnviar) {

                if (ValidadorNotificacionAsincronica.cumpleCondiciones(notificacion)) {
                    builder.editarTexto(notificacion.getTexto());
                }

                this.notificar(usuario);

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

