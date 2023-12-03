package models.entities.domain.notificaciones.medioEnvio;

import models.entities.domain.registro.Usuario;

public interface AdapterWhatsapp  {

    public void enviarNotificacionA(Usuario usuario, String texto);

}
