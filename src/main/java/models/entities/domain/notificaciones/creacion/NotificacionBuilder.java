package models.entities.domain.notificaciones.creacion;

import models.entities.domain.config.Config;
import models.entities.domain.config.ConfigTextoNotificacion;
import models.entities.domain.incidentes.Incidente;
import models.entities.domain.notificaciones.Notificacion;

import java.time.LocalDateTime;

public class NotificacionBuilder {

    private String texto;

    public Notificacion construirNotificacion(LocalDateTime hora) {

        return new Notificacion(texto, hora);
    }

    public void construirTexto(String texto) {

            this.texto = texto;

    }

    public void construirTexto(Incidente incidente, ContextoDeIncidente contexto) {

        String rutaArchivo = Config.RUTA_ARCHIVOS + "textos_incidente.json";


        switch (contexto) {

            case CREACION -> this.texto = ConfigTextoNotificacion.CREACION + incidente.getServicioAfectado().obtenerTextoRelevante();

            case SUGERENCIA_DE_REVISION -> this.texto = ConfigTextoNotificacion.SUGERENCIA + incidente.getServicioAfectado() + " en " + incidente.getLocalizacion();

            case CIERRE -> this.texto = this.texto = ConfigTextoNotificacion.CIERRE + incidente.getServicioAfectado().obtenerTextoRelevante() + "!";
        }

    }

    public void editarTexto(String nuevoTexto) {

        this.texto = this.texto + " - " + nuevoTexto + "\n";
    }
}
