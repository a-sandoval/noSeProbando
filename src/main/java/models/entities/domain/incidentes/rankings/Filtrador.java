package models.entities.domain.incidentes.rankings;

import models.repositories.datos.RepositorioIncidentes;
import models.entities.domain.entidades.Entidad;
import models.entities.domain.incidentes.Estado;
import models.entities.domain.incidentes.Incidente;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Filtrador {

    public ArrayList<Incidente> filtrarIncidentesUltimaSemana(){
        RepositorioIncidentes repositorioIncidentes = RepositorioIncidentes.getInstance();
        return (ArrayList<Incidente>) repositorioIncidentes.filtrarUltimaSemana();
    }

    public Map<Entidad, List<Incidente>> separarPorEntidadAfectada(ArrayList<Incidente> incidentes){

        return this.filtrarIncidentesUltimaSemana().stream().collect((Collectors.groupingBy(Incidente::obtenerEntidad)));

    }

    public ArrayList<ArrayList<Incidente>> obtenerIncidentesPorPrestacion(ArrayList<Incidente> incidentes){
        ArrayList<ArrayList<Incidente>> incidentesPorPrestacion = new ArrayList<>();


        for (Incidente incidente : incidentes) {
            boolean encontrada = false;

            // Buscar la sublista correspondiente a la prestación del incidente
            for (ArrayList<Incidente> prestacion : incidentesPorPrestacion) {
                if (prestacion.get(0).getServicioAfectado().equals(incidente.getServicioAfectado())) {
                    // Agregar el incidente a la sublista existente
                    prestacion.add(incidente);
                    encontrada = true;
                    break;
                }
            }

            // Si no se encontró una sublista existente, crear una nueva y agregar el incidente
            if (!encontrada) {
                ArrayList<Incidente> nuevaPrestacion = new ArrayList<>();
                nuevaPrestacion.add(incidente);
                incidentesPorPrestacion.add(nuevaPrestacion);
            }
        }

        return incidentesPorPrestacion;
    }

    public ArrayList<ArrayList<Incidente>> obtenerIncidentesPorDia(ArrayList<Incidente> incidentes){
        ArrayList<ArrayList<Incidente>> incidentesPorDia = new ArrayList<>(DayOfWeek.values().length);
        for (int i = 0; i < DayOfWeek.values().length; i++) {
            incidentesPorDia.add(new ArrayList<>());
        }

        // Separar los incidentes por día de la semana
        for (Incidente incidente : incidentes) {
            DayOfWeek dayOfWeek = incidente.getFechaReporte().getDayOfWeek();
            int indiceDia = dayOfWeek.getValue() - 1; // Índice basado en 0
            incidentesPorDia.get(indiceDia).add(incidente);
        }

        incidentesPorDia.removeIf(diaSemana -> diaSemana.isEmpty());

        return incidentesPorDia;
    }

    //public ArrayList<Incidente> filtrarOcurridosUltimaSemana(ArrayList<Incidente> incidentes){
        /*LocalDateTime inicioUltimaSemana = LocalDateTime.now().minusWeeks(1);
        ArrayList<Incidente> incidentes1 = incidentes.stream().filter(incidente -> incidente.getFechaReporte().isAfter(inicioUltimaSemana)).collect(Collectors.toCollection(ArrayList::new));
        if (incidentes1.isEmpty()) {
            throw new IllegalStateException("AAAAAAAAAAAAAAA."); // Lanzar una excepción
            }
        return incidentes1;

         */

   // }

    public ArrayList<Incidente> filtrarRepetidosEn24hs(ArrayList<Incidente> incidentesSemanales){
        ArrayList<ArrayList<Incidente>> incidentesPorPrestacion = this.obtenerIncidentesPorPrestacion(incidentesSemanales);
        /*if (incidentesPorPrestacion.isEmpty()) {
            throw new IllegalStateException("."); // Lanzar una excepción
        }*/
        for(ArrayList<Incidente> incidentesMismaPrestacion: incidentesPorPrestacion) {
            ArrayList<ArrayList<Incidente>> incidentesPorDia = this.obtenerIncidentesPorDia(incidentesMismaPrestacion);

            for(ArrayList<Incidente> incidentesMismoDia: incidentesPorDia){

                incidentesMismoDia.stream().filter(incidente -> incidente.getEstado().getEstado() == Estado.RESUELTO || filtrarActivos(incidente));
            }

            incidentesMismaPrestacion = incidentesPorDia.stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toCollection(ArrayList::new));

        }

        ArrayList<Incidente> incidentesFiltrados = incidentesPorPrestacion.stream()
                .flatMap(List::stream)
                .collect(Collectors.toCollection(ArrayList::new));


        return incidentesFiltrados;
    }

    private static boolean primerActivo = false;
    private static boolean filtrarActivos(Incidente incidente){
        if (incidente.getEstado().getEstado() == Estado.ACTIVO) {
            if (!primerActivo) {
                primerActivo = true;
                return true;
            }
            return false;
        }
        return true;
    }
}
