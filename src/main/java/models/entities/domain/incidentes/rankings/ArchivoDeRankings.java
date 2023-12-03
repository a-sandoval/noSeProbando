package models.entities.domain.incidentes.rankings;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import models.entities.domain.entidades.Entidad;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ArchivoDeRankings implements Job {

    private Ranking ranking;

    public void execute(JobExecutionContext context) {

        this.generarArchivoPorCantidadDeReportes();
        this.generarArchivoPorPromedioCierre();

    }
    private List<String[]> reescribirDatosPreviosRanking(String rutaArchivoRankings) {

        List<String[]> existingData = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(rutaArchivoRankings))) {
            existingData = reader.readAll();
        } catch (IOException | CsvException e) {
            // El archivo no existe o no se pudo leer, lo dejamos vacío
        }

        return existingData;

    }

    private String[] obtenerFechasSemanaActual() {
        // Obtener la fecha de inicio y fin de la semana actual (de lunes a domingo)
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate fechaInicioSemana = LocalDate.now().with(java.time.DayOfWeek.MONDAY); // Lunes actual
        String fechaInicioSemanaStr = fechaInicioSemana.format(dateFormat);

        LocalDate fechaFinSemana = fechaInicioSemana.plusDays(6); // Domingo actual
        String fechaFinSemanaStr = fechaFinSemana.format(dateFormat);

        return new String[]{fechaInicioSemanaStr, fechaFinSemanaStr};
    }

    public void cambiarRanking(Ranking ranking){
        this.ranking = ranking;
    }
    private void generarArchivoPorCantidadDeReportes() {

        Ranking cantidadDeIncidentes = new CantidadDeIncidentes();
        this.cambiarRanking(cantidadDeIncidentes);

        ArrayList<Entidad> rankingPorCantidadDeReportes = ranking.generarRanking();

        String rutaArchivoRankings = "main/java/datos/rankingReportes.csv";

        try (CSVWriter writer = new CSVWriter(new FileWriter(rutaArchivoRankings))) {

            List<String[]> existingData = this.reescribirDatosPreviosRanking(rutaArchivoRankings);

            String[] weekDates = this.obtenerFechasSemanaActual();

            writer.writeNext(new String[]{"Semana", weekDates[0] + " a " + weekDates[1]});


            writer.writeNext(Entidad.obtenerEncabezadosCSV("INFORME_POR_CANTIDAD_INCIDENTES"));

            for (Entidad entidad : rankingPorCantidadDeReportes) {
                writer.writeNext(entidad.obtenerDatosCSV("INFORME_POR_CANTIDAD_INCIDENTES"));
            }

            writer.writeAll(existingData);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void generarArchivoPorPromedioCierre(){

        Ranking promedioDeCierre = new PromedioDeCierre();
        this.cambiarRanking(promedioDeCierre);

        ArrayList<Entidad> rankingPorPromedioDeCierre = ranking.generarRanking();

        String rutaArchivoRankings = "main/java/datos/rankingPromedioCierre.csv";

        try (CSVWriter writer = new CSVWriter(new FileWriter(rutaArchivoRankings))) {

            List<String[]> existingData = this.reescribirDatosPreviosRanking(rutaArchivoRankings);

            String[] weekDates = this.obtenerFechasSemanaActual();

            writer.writeNext(new String[]{"Semana", weekDates[0] + " a " + weekDates[1]});

            writer.writeNext(Entidad.obtenerEncabezadosCSV("INFORME_POR_PROMEDIO_CIERRE"));

            for (Entidad entidad : rankingPorPromedioDeCierre) {
                writer.writeNext(entidad.obtenerDatosCSV("INFORME_POR_PROMEDIO_CIERRE"));
            }

            writer.writeAll(existingData);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
