package models.entities.domain.notificaciones.medioEnvio;

import models.entities.domain.registro.Usuario;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class AdapterTwillio implements AdapterWhatsapp {

    public static final String ACCOUNT_SID = "AC39d46828c8b731cb87c7ad3631fce232";
    public static final String AUTH_TOKEN = "3896ad9fb5a70218ef1786e163f3923d";


    @Override
    public void enviarNotificacionA(Usuario usuario, String texto) {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String telefonoDeEnvio = "5491138920468";
        String telefonoRecepcion = usuario.getNumeroTelefono();


        Message message = Message.creator(
                        new PhoneNumber(telefonoRecepcion),
                        new PhoneNumber(telefonoDeEnvio),
                        texto)
                .create();
    }

}
