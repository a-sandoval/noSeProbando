package tests.domain.converters;

import converters.MedioNotificacionAttributeConverter;
import junit.framework.Assert;
import models.entities.domain.notificaciones.medioEnvio.Mail;
import models.entities.domain.notificaciones.medioEnvio.MedioNotificacion;
import models.entities.domain.notificaciones.medioEnvio.WhatsApp;
import org.junit.jupiter.api.Test;

public class ConvertersTest {

    @Test
    public void testConverterMedioNotificacionWhatsApp(){
        MedioNotificacion medioWpp = new WhatsApp();
        MedioNotificacionAttributeConverter converter = new MedioNotificacionAttributeConverter();
        String resultadoWpp = converter.convertToDatabaseColumn(medioWpp);

        Assert.assertEquals("WhatsApp", resultadoWpp);
    }

    @Test
    public void testConverterMedioNotificacionMail(){
        MedioNotificacion medioMail = new Mail();
        MedioNotificacionAttributeConverter converter = new MedioNotificacionAttributeConverter();
        String resultadoMail = converter.convertToDatabaseColumn(medioMail);

        Assert.assertEquals("Mail", resultadoMail);
    }
}
