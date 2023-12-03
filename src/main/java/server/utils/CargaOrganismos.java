package server.utils;

import models.entities.domain.comunidad.Comunidad;
import models.entities.domain.entidades.Entidad;
import models.entities.domain.entidades.Establecimiento;
import models.entities.domain.entidades.OrganismoDeControl;
import models.entities.domain.entidades.PrestadoraDeServicio;
import models.entities.domain.services.georef.entities.Ubicacion;
import models.entities.domain.servicios.PrestacionDeServicio;
import models.entities.domain.servicios.Servicio;
import models.repositories.datos.*;
import server.Server;

public class CargaOrganismos {
    public static void main(String[] args) {
        // Gral
       /* OrganismoDeControl organismo =  new OrganismoDeControl();

        RepositorioOrganismosDeControl repoOrganismos = new  RepositorioOrganismosDeControl();

        repoOrganismos.guardar(organismo);
        repoOrganismos.actualizar(organismo);
        organismo.setNombre("CNRT");
        organismo.setCUIT("30715255703");

        */

        // Servicios
        Servicio banioMujeres = new Servicio("banio", "banio exclusivo para mujeres");
        Servicio banioHombres = new Servicio("banio", "banio exclusivo para hombres");
        Servicio escalerasMecanicas = new Servicio("escaleras", "escaleras mecánicas con maximo de 100 personas");
        Servicio ascensor = new Servicio("ascensor", "ascensor de 120kg de máximo");

        RepositorioServicios repositorioServicios = new RepositorioServicios();

        repositorioServicios.guardar(banioMujeres);
        repositorioServicios.guardar(banioHombres);
        repositorioServicios.guardar(escalerasMecanicas);
        repositorioServicios.guardar(ascensor);

        /*

         */
        // Establecimiento
        /*
        Establecimiento retiro = new Establecimiento("Retiro", new Ubicacion(-34.5927,-58.3786));
        Establecimiento tigre = new Establecimiento("Tigre", new Ubicacion(-34.4255,-58.5704));
        Establecimiento once = new Establecimiento("Estacion Once", new Ubicacion( -34.6097,-58.4083  ));
        Establecimiento liniers = new Establecimiento("Estacion Liniers", new Ubicacion( -34.6432, -58.5199));

        RepositorioEstablecimientos repositorioEstablecimientos = new RepositorioEstablecimientos();

        repositorioEstablecimientos.guardar(retiro);
        repositorioEstablecimientos.guardar(tigre);
        repositorioEstablecimientos.guardar(once);
        repositorioEstablecimientos.guardar(liniers); */

        Establecimiento retiro = (Establecimiento) new RepositorioEstablecimientos().buscar(1L);
        Establecimiento tigre = (Establecimiento) new RepositorioEstablecimientos().buscar(2L);

        Establecimiento once = (Establecimiento) new RepositorioEstablecimientos().buscar(3L);

        Establecimiento liniers = (Establecimiento) new RepositorioEstablecimientos().buscar(4L);


        // Prestacion de servicio
        PrestacionDeServicio banioMujeresEnEstacionMitre = new PrestacionDeServicio();
        PrestacionDeServicio ascensorEstacionSarmiento = new PrestacionDeServicio();
        PrestacionDeServicio escalerasMecanicasEstacionMitre = new PrestacionDeServicio();

        banioMujeresEnEstacionMitre.setNombre("Banio de mujeres en Estacion Retiro");
        banioMujeresEnEstacionMitre.setServicio(banioMujeres);
        banioMujeresEnEstacionMitre.setEstablecimiento(retiro);

        ascensorEstacionSarmiento.setNombre("Ascensor en Estacion Liniers");
        ascensorEstacionSarmiento.setServicio(ascensor);
        ascensorEstacionSarmiento.setEstablecimiento(liniers);

        RepositorioPrestacionesDeServicio repositorioPrestacionesDeServicio = new RepositorioPrestacionesDeServicio();

        repositorioPrestacionesDeServicio.guardar(banioMujeresEnEstacionMitre);
        repositorioPrestacionesDeServicio.guardar(ascensorEstacionSarmiento);
        repositorioPrestacionesDeServicio.guardar(escalerasMecanicasEstacionMitre);

        // Prestaciones de servicio
/*
        PrestadoraDeServicio prestadora = new PrestadoraDeServicio();

        RepositorioPrestadorasDeServicio repoPrestadoras = new RepositorioPrestadorasDeServicio();

        repoPrestadoras.guardar(prestadora);
        repoPrestadoras.actualizar(prestadora);
        prestadora.setNombre("Trenes Argentinos");
        prestadora.setOrganismoDeControl(organismo);


        // Comunidades
        Comunidad comunidad = new Comunidad();
        Comunidad unaComunidad = new Comunidad();
        Comunidad otraComunidad = new Comunidad();

        comunidad.setNombre("UTNIANOS");
        comunidad.setDescripcion("Interesados en ver los servicios alrededor de nuestra querida universidad");
        unaComunidad.setNombre("Diseñadoras, no graficas");
        unaComunidad.setDescripcion("Aquellas personas a las que nunca les anda Javalin");
        otraComunidad.setNombre("Comedores de helados en la linea A");
        otraComunidad.setDescripcion("Aca importa mucho el funcionamiento de la linea A, especialmente en verano");

        new RepositorioComunidades().guardar(comunidad);
        new RepositorioComunidades().guardar(unaComunidad);
        new RepositorioComunidades().guardar(otraComunidad);

        //Entidades
        Entidad entidad1 = new Entidad();
        Entidad entidad2 = new Entidad();

        RepositorioEntidades repositorioEntidades =new RepositorioEntidades();

        entidad1.setNombre("Linea Mitre");
        entidad2.setNombre("Linea Sarmiento");

        repositorioEntidades.guardar(entidad1);
        repositorioEntidades.guardar(entidad2);
        repositorioEntidades.actualizar(entidad1);
        repositorioEntidades.actualizar(entidad2);

        prestadora.aniadirEntidad(entidad1);
        prestadora.aniadirEntidad(entidad2);

        entidad1.agregarEstablecimientos(retiro,tigre);
        entidad2.agregarEstablecimientos(once, liniers);

        organismo.aniadirPrestadoraControlada(prestadora);
    }
    */

    }
}
