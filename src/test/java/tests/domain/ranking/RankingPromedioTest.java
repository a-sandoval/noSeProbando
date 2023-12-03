package tests.domain.ranking;

import models.repositories.datos.RepositorioComunidades;
import models.repositories.datos.RepositorioIncidentes;
import models.repositories.datos.RepositorioUsuarios;
import models.entities.domain.comunidad.Comunidad;
import models.entities.domain.entidades.Entidad;
import models.entities.domain.entidades.Establecimiento;
import models.entities.domain.incidentes.Incidente;
import models.entities.domain.incidentes.rankings.CantidadDeIncidentes;
import models.entities.domain.incidentes.rankings.PromedioDeCierre;
import models.entities.domain.notificaciones.medioEnvio.WhatsApp;
import models.entities.domain.notificaciones.tiempoDeEnvio.ModoRecepcion;
import models.entities.domain.notificaciones.tiempoDeEnvio.PreferenciaEnvioNotificacion;
import models.entities.domain.registro.Usuario;
import models.entities.domain.services.georef.entities.Ubicacion;
import models.entities.domain.servicios.PrestacionDeServicio;
import models.entities.domain.servicios.Servicio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RankingPromedioTest {

    Usuario usuario = new Usuario("Pepita", null, null);

    Comunidad comunidad = new Comunidad();
    RepositorioComunidades repoComunidades = new RepositorioComunidades();
    RepositorioUsuarios repoUsuarios = new RepositorioUsuarios();

    PreferenciaEnvioNotificacion pref = new PreferenciaEnvioNotificacion(new WhatsApp(), ModoRecepcion.SINCRONICA);

    Servicio banios = new Servicio("Baños", "Baños de hombres y mujeres");

    Establecimiento sedeLugano = new Establecimiento("Sede Lugano", new Ubicacion(2,2));
    Establecimiento sedeMedrano = new Establecimiento("Sede Medrano", new Ubicacion(23,23));

    Establecimiento facultadIng = new Establecimiento("Facultad de Ingeniería", new Ubicacion(20,20));


    PrestacionDeServicio prestacion = new PrestacionDeServicio(banios, sedeLugano);
    PrestacionDeServicio prestacion2 = new PrestacionDeServicio(banios, sedeMedrano);

    PrestacionDeServicio prestacion3 = new PrestacionDeServicio(banios, facultadIng);

    Entidad UTN = new Entidad();

    Entidad entidadConMuyMalPromedio = new Entidad();

    @BeforeEach
    void inicializar() {

        usuario.setPreferencias(pref);
        repoUsuarios.guardar(usuario);

        UTN.setNombre("UTN");
        UTN.agregarEstablecimientos(sedeLugano, sedeMedrano);
        sedeLugano.setEntidad(UTN);
        sedeMedrano.setEntidad(UTN);
        usuario.agregarEntidadesDeInteres(UTN);

        entidadConMuyMalPromedio.setNombre("UBA");
        entidadConMuyMalPromedio.agregarEstablecimientos(facultadIng);
        facultadIng.setEntidad(entidadConMuyMalPromedio);
        usuario.agregarEntidadesDeInteres(entidadConMuyMalPromedio);

        repoComunidades.guardar(comunidad);

    }

    @Test
    public void testIncidentesGuardados() {
        LocalDateTime fechaDeReporte = LocalDateTime.of(2023, 10, 7, 0, 0);
        Incidente incidente1 = new Incidente(prestacion, usuario, comunidad, "Incidente 1636732768");
        incidente1.setFechaReporte(fechaDeReporte);

        RepositorioIncidentes repo = RepositorioIncidentes.getInstance();
        System.out.println(repo.getIncidentes().size());

        Incidente incidente = repo.getIncidentes().get(0);
        System.out.println(incidente.getDescripcion());
    }
    @Test
    public void testIncidentesSemanales() {
        LocalDateTime fechaDeReporte = LocalDateTime.of(2023, 9, 5, 0, 0);
        Incidente incidente1 = new Incidente(prestacion, usuario, comunidad, "Incidente 1636732768");
        incidente1.setFechaReporte(fechaDeReporte);

        RepositorioIncidentes repo = RepositorioIncidentes.getInstance();
        System.out.println(repo.getIncidentes().size());

        List<Incidente> incidente = repo.filtrarUltimaSemana();
        System.out.println(incidente.get(0).getDescripcion());

    }

    @Test
    public void testRankingPorPromedioDeCierre() throws MessagingException {
        PromedioDeCierre rankingpromedioDeCierre = new PromedioDeCierre();

        //todos los incidentes se reportan en la misma fecha
        LocalDateTime fechaDeReporte = LocalDateTime.of(2023, 9, 5, 0, 0);

        Incidente incidente1 = new Incidente(prestacion, usuario, comunidad, "Incidente 1");
        LocalDateTime tiempoAntes = LocalDateTime.of(2023, 10, 7, 0, 0);
        incidente1.setFechaReporte(fechaDeReporte);

        Incidente incidente2 = new Incidente(prestacion2, usuario, comunidad, "Incidente 2");
        LocalDateTime tiempoDespues = LocalDateTime.of(2023, 10, 7, 8, 0);
        incidente2.setFechaReporte(fechaDeReporte);

        Incidente incidente3 = new Incidente(prestacion3, usuario, comunidad, "Incidente 3");
        incidente3.setFechaReporte(fechaDeReporte);
        LocalDateTime tiempoMuchoDespues =  LocalDateTime.of(2023, 12, 7, 7, 0);

        incidente1.cerrarse(usuario);
        incidente1.setFechaResolucion(tiempoAntes);
        incidente2.cerrarse(usuario);
        incidente2.setFechaResolucion(tiempoDespues);
        incidente3.cerrarse(usuario);
        incidente3.setFechaResolucion(tiempoMuchoDespues);

        ArrayList<Entidad> ranking = rankingpromedioDeCierre.generarRanking();

        ranking.forEach(resultado -> {System.out.println("Promedio en minutos:" + resultado.getPromedioCierre() + " Entidad:" + resultado.getNombre());});

        Assertions.assertEquals(entidadConMuyMalPromedio, ranking.get(0));
    }

    @Test
    public void testRankingPorMayorCantidadDeIncidentes() throws MessagingException{
        CantidadDeIncidentes rankingCantidadDeIncidentes = new CantidadDeIncidentes();

        LocalDateTime fechaDeReporte = LocalDateTime.of(2023, 9, 5, 0, 0);
        //todos los incidentes se reportan en la misma fecha

        Incidente incidente1 = new Incidente(prestacion, usuario,  null, "");
        LocalDateTime tiempoAntes = LocalDateTime.of(2023, 9, 5, 0, 0);
        incidente1.setFechaReporte(fechaDeReporte);

        Incidente incidente2 = new Incidente(prestacion2, usuario, null, "");
        LocalDateTime tiempoDespues = LocalDateTime.of(2023, 9, 7, 8, 0);
        incidente2.setFechaReporte(fechaDeReporte);

        Incidente incidente3 = new Incidente(prestacion3, usuario,null, "");
        incidente3.setFechaReporte(fechaDeReporte);
        LocalDateTime tiempoMuchoDespues =  LocalDateTime.of(2023, 9, 7, 7, 0);

        Incidente ueReBaqueteadaLaPrestacion1 = new Incidente(prestacion, usuario,null, "");
        ueReBaqueteadaLaPrestacion1.setFechaReporte(fechaDeReporte);

        incidente1.cerrarse(usuario);
        incidente1.setFechaResolucion(tiempoAntes);
        incidente2.cerrarse(usuario);
        incidente2.setFechaResolucion(tiempoDespues);
        incidente3.cerrarse(usuario);
        incidente3.setFechaResolucion(tiempoMuchoDespues);

        RepositorioIncidentes.getInstance().getIncidentesForTest().forEach(i-> {System.out.println(i.getFechaReporte());});

        ArrayList<Entidad> ranking = rankingCantidadDeIncidentes.generarRanking();
        System.out.println(ranking.get(0).getNombre());
        ranking.forEach(resultado -> {System.out.println( "Entidad:" + resultado.getNombre());});
        //Assertions.assertEquals(entidadConMuyMalPromedio, ranking.get(0));
    }
}