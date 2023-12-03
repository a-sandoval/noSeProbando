package tests.domain.registro;

import models.entities.domain.notificaciones.medioEnvio.WhatsApp;
import models.entities.domain.notificaciones.tiempoDeEnvio.ModoRecepcion;
import models.entities.domain.notificaciones.tiempoDeEnvio.PreferenciaEnvioNotificacion;
import models.entities.domain.registro.Contrasenia;
import models.entities.domain.registro.Usuario;
import models.entities.domain.registro.Validador;
import models.entities.domain.registro.condicionesContra.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContraseniaTests {

    private Contrasenia contraseniaNOAcortable = new Contrasenia("1234    ");
    private Contrasenia contraseniaAcortable = new Contrasenia("12345678  ");
    private Contrasenia contraseniaCorta = new Contrasenia("1234");
    private Contrasenia contraseniaLarga = new Contrasenia("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
    private Contrasenia contraseniaConRepes = new Contrasenia("1111234678");
    private Contrasenia contraseniaSinRepes = new Contrasenia("12345678");
    private Contrasenia contraseniaEspecial = new Contrasenia("123#");
    private static Contrasenia contraseniaValida = new Contrasenia("Abcd12345_");
    private static String usuario = "pepito";

    private Validador validador = new Validador();
    private Longitud longitud = new Longitud();
    private RepeticionCaracteres repeticiones = new RepeticionCaracteres();
    private UsoDeCredenciales usoDeCredenciales = new UsoDeCredenciales();
    private UsoReiterado usoReiterado = new UsoReiterado();

    private static PreferenciaEnvioNotificacion preferencia = new PreferenciaEnvioNotificacion(new WhatsApp(), ModoRecepcion.ASINCRONICA);

    public ContraseniaTests() throws IOException {
    }

    private void instanciarCondiciones() {
        this.validador.agregarCondiciones(longitud, repeticiones, usoDeCredenciales, usoReiterado);
    }
    private static Usuario usuarioValido = new Usuario();

    @BeforeAll
    public static void inicializar() {
        usuarioValido.setNombreDeUsuario("Pepito");
        usuarioValido.setContrasenia(contraseniaValida);
        usuarioValido.setEmail("pepito@gmail.com");
        usuarioValido.setPreferencias(preferencia);
    }

    @Test
    public void testReducirEspaciosCuandoSeaNecesario()
    {
        contraseniaNOAcortable.reducirEspacios();

        Assertions.assertEquals(contraseniaNOAcortable.getContrasenia(), "1234");
    }

    @Test //Verificando que las contraseñas cortas lancen una excepcion
    public void testLanzarExcepcionContraCorta()
    {
        assertThrows(ContraseniaNoCumpleConLongitudException.class, () -> {
            longitud.cumpleConLongitud(contraseniaCorta);
        }, "Se espera que se lance una excepcion");
    }

    public void testClaveMuyLarga()
    {
        Assertions.assertThrows(ContraseniaNoCumpleConLongitudException.class, () ->
        {longitud.cumpleConLongitud(contraseniaLarga);
        }, "Se espera que se lance una excepcion");
    }

    @Test
    public void testContraseniaEsValida(){
        this.instanciarCondiciones();
        Assertions.assertFalse( contraseniaCorta.esValida(usuario));
    }
    @Test
    public void testContraseniaNoEsValida(){
        this.instanciarCondiciones();
        Assertions.assertFalse(contraseniaCorta.esValida(usuario));
    }
    @Test
    public void testClaveConRepeticiones()
    {
        Assertions.assertTrue(repeticiones.tieneCaracteresRepetidosXVeces(contraseniaConRepes.getContrasenia(), 3));
    }
    @Test
    public void testClaveConRepeticionesException()
    {
        Assertions.assertThrows(ContraseniaRepiteCaracteresException.class, () -> {
            repeticiones.noRepiteCaracteres(contraseniaConRepes);
        }, "Se espera que se lance una excepcion");
    }
    @Test
    public void testClaveSinRepeticionesException()
    {
        Assertions.assertTrue(repeticiones.noRepiteCaracteres(contraseniaSinRepes));
    }
    @Test
    public void testClaveTieneCaracEspecial()
    {
        Assertions.assertTrue(contraseniaEspecial.tieneCaracterEspecial());
    }
    @Test
    public void testNoTieneCaracEspecial()
    {
        Assertions.assertFalse(contraseniaCorta.tieneCaracterEspecial());
    }
}

