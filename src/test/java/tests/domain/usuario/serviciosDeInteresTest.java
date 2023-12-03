package tests.domain.usuario;

import models.entities.domain.entidades.Entidad;
import models.entities.domain.entidades.Establecimiento;
import models.entities.domain.notificaciones.medioEnvio.WhatsApp;
import models.entities.domain.notificaciones.tiempoDeEnvio.ModoRecepcion;
import models.entities.domain.notificaciones.tiempoDeEnvio.PreferenciaEnvioNotificacion;
import models.entities.domain.registro.Contrasenia;
import models.entities.domain.registro.Usuario;
import models.entities.domain.services.georef.entities.Ubicacion;
import models.entities.domain.servicios.PrestacionDeServicio;
import models.entities.domain.servicios.Servicio;
import models.repositories.datos.RepositorioPrestacionesDeServicio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class serviciosDeInteresTest {

    private Contrasenia unUsuarioContra = new Contrasenia(("buenasTardes2"));
    private PreferenciaEnvioNotificacion preferencia = new PreferenciaEnvioNotificacion(new WhatsApp(), ModoRecepcion.SINCRONICA);
    private Usuario unUsuario = new Usuario();

    private Entidad lineaMitre = new Entidad();
    private Establecimiento Retiro = new Establecimiento();
    private Establecimiento Tigre = new Establecimiento();

    private Servicio escaleraMecanica = new Servicio("medio elevación", "lo usas para subir");
    private Servicio banioMujeres = new Servicio("banio", "banio para mujeres");

    private PrestacionDeServicio brindarEscalera = new PrestacionDeServicio();
    private PrestacionDeServicio brindarBanio = new PrestacionDeServicio();

    private PrestacionDeServicio brindarBanioTigre = new PrestacionDeServicio();

    @BeforeEach

    public void instanciarUsuarioYEntidades() {

        unUsuario.setNombreDeUsuario("marsoteras");
        unUsuario.setContrasenia(unUsuarioContra);
        unUsuario.setEmail("marsoteras@gmail.com");

        lineaMitre.setNombre("Linea Mitre");

        brindarEscalera.setServicio(escaleraMecanica);
        brindarEscalera.setEstablecimiento(Retiro);

        brindarEscalera.setServicio(banioMujeres);
        brindarEscalera.setEstablecimiento(Retiro);

        brindarBanioTigre.setServicio(banioMujeres);
        brindarEscalera.setEstablecimiento(Tigre);


        Retiro.setNombre("Estacion Retiro");
        Retiro.setUbicacionGeografica(new Ubicacion( -34.5833300, 58.3833300));
        Tigre.setNombre("Estacion Tigre - Fin del recorrido linea Mitre");
        Tigre.setUbicacionGeografica(new Ubicacion(-34.4237,-58.5794));
        Retiro.brindarServicios(brindarEscalera, brindarBanio);
        Tigre.brindarServicios(brindarBanioTigre);

        lineaMitre.agregarEstablecimientos(Retiro, Tigre);
        unUsuario.agregarEntidadesDeInteres(lineaMitre);

    }

    private void deshabilitarPrestaciones() {
        brindarBanio.deshabilitar();
        brindarBanioTigre.deshabilitar();
        brindarEscalera.deshabilitar();

        new RepositorioPrestacionesDeServicio().actualizar(brindarBanioTigre);
        new RepositorioPrestacionesDeServicio().actualizar(brindarEscalera);

    }
    @Test
    public void testObtenerServiciosDeInteresIncidentados(){

        this.deshabilitarPrestaciones();
        ArrayList<PrestacionDeServicio> listaEsperada = new ArrayList<>();

        listaEsperada.add(brindarEscalera);
        listaEsperada.add(brindarBanio);
        listaEsperada.add(brindarBanioTigre);

        Assertions.assertEquals(unUsuario.prestacionDeServiciosDeInteres(), listaEsperada);


    }

}
