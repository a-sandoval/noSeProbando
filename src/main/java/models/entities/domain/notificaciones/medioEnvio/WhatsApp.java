package models.entities.domain.notificaciones.medioEnvio;

import models.entities.domain.registro.Usuario;


public class WhatsApp implements MedioNotificacion {

    AdapterWhatsapp adapter;

    @Override
    public void enviarNotificacionA(Usuario usuario, String texto) {

        adapter.enviarNotificacionA(usuario, texto);
    }
}
