package models.entities.domain.entidades;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import models.repositories.datos.RepositorioOrganismosDeControl;
import models.repositories.datos.RepositorioPrestadorasDeServicio;
import models.entities.domain.config.Config;
import lombok.Getter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.io.File;
@Getter
public class cargaEntidadesyOrgDeControl {

    protected ArrayList<OrganismoDeControl> organismosRegistrados = new ArrayList<>();

    public void cargarEntidadesYOrgDeControl(String archivo) throws CsvValidationException {

        RepositorioOrganismosDeControl repositorioOrganismosDeControl = RepositorioOrganismosDeControl.getInstance();

        RepositorioPrestadorasDeServicio repositorioPrestadoras = RepositorioPrestadorasDeServicio.getInstance();

        //String archivo = Config.RUTA_CSV;

        try (CSVReader reader = new CSVReader(new FileReader(archivo))) {
            String[] linea;

            while ((linea = reader.readNext()) != null) {
                //Formato CSV:
                //Organismo de Control, CUIT, Entidad Prestad ora

                if(repositorioOrganismosDeControl.estaRegistrado(linea[1]))
                {
                    final String cuit = linea[1];

                    Optional<OrganismoDeControl> organismoEncontrado = organismosRegistrados.stream()
                            .filter(organismo -> organismo.getCUIT().equals(cuit))
                            .findFirst();

                    if(organismoEncontrado.isPresent()) {
                        PrestadoraDeServicio prestadora = new PrestadoraDeServicio(linea[2]);
                        organismoEncontrado.get().aniadirPrestadoraControlada(prestadora);
                        repositorioPrestadoras.guardar(prestadora);
                    }


                }
                else {
                    OrganismoDeControl organismo = new OrganismoDeControl();
                    organismo.setNombre(linea[0]);
                    organismo.setCUIT(linea[1]);
                    organismosRegistrados.add(organismo);

                    PrestadoraDeServicio prestadora = new PrestadoraDeServicio();
                    prestadora.setNombre(linea[2]);
                    organismo.aniadirPrestadoraControlada(prestadora);

                    repositorioOrganismosDeControl.guardar(organismo);
                    repositorioPrestadoras.guardar(prestadora);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Boolean organismoYaRegistrado(String CUIT){
        ArrayList<String> cuitsOrganismosRegistrados = (ArrayList<String>) organismosRegistrados.stream()
                .map(OrganismoDeControl::getCUIT)
                .toList();

        return (cuitsOrganismosRegistrados.contains(CUIT));
    }
}