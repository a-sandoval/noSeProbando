package tests.domain.csv;

import com.opencsv.exceptions.CsvValidationException;
import models.entities.domain.entidades.cargaEntidadesyOrgDeControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
/*
public class CSVTest {
    @Test
    public void testCargaOrganismosDeControl() throws CsvValidationException, IOException {
        cargaEntidadesyOrgDeControl carga = new cargaEntidadesyOrgDeControl();
        carga.cargarEntidadesYOrgDeControl();

        ArrayList<String> nombresDeOrganismosCargados = (ArrayList<String>) carga.getOrganismosRegistrados().stream().map(x -> x.getNombre()).collect(Collectors.toList());
        ArrayList<String> nombresDeOrganismos = new ArrayList<String>(Arrays.asList("CNRT", "ENARGAS", "ENRE", "ENACOM", "ENOHSA", "Banco Central"));

        Assertions.assertTrue(nombresDeOrganismos.containsAll(nombresDeOrganismosCargados));
    }

}*/

