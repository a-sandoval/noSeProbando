package converters;

import models.entities.domain.notificaciones.medioEnvio.Mail;
import models.entities.domain.notificaciones.medioEnvio.MedioNotificacion;
import models.entities.domain.notificaciones.medioEnvio.WhatsApp;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MedioNotificacionAttributeConverter implements AttributeConverter<MedioNotificacion,String> {
    @Override
    public String convertToDatabaseColumn(MedioNotificacion medioNotificacion) {
        if(medioNotificacion == null) return null;

        String s = "";

        if (medioNotificacion instanceof Mail){
            s = "Mail";
        }
        if (medioNotificacion instanceof WhatsApp){
            s = "WhatsApp";
        }
        return s;
    }

    @Override
    public MedioNotificacion convertToEntityAttribute(String s) {

        if(s==null) return null;
        MedioNotificacion medio;

        switch(s) {
            case "WhatsApp":
                medio = new WhatsApp();
                break;
            case "Mail":
                medio = new Mail();
                break;
            default:
                medio = null;
        }
        return medio;
    }
}