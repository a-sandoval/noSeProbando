package models.entities.domain.notificaciones.medioEnvio;

import models.entities.domain.registro.Usuario;

import javax.mail.MessagingException;

public interface MedioNotificacion {


    public void enviarNotificacionA(Usuario usuario, String texto) throws MessagingException;
}
