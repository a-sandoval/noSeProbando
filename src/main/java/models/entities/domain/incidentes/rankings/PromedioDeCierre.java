package models.entities.domain.incidentes.rankings;

import models.entities.domain.entidades.Entidad;
import models.entities.domain.incidentes.Incidente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromedioDeCierre extends Ranking{

    public ArrayList<Entidad> generarRanking() {

        Map<Entidad, List<Incidente>> incidentesPorEntidad = filtradorDeIncidentes.separarPorEntidadAfectada(incidentesSemanales);
        Map<Entidad, Double> promedioCierrePorEntidad = this.generarPromedioCierrePorEntidad(incidentesPorEntidad);

        ArrayList<Entidad> ranking = new ArrayList<>(promedioCierrePorEntidad.keySet());
        ranking.sort(new RepeticionEntidadesComparador(promedioCierrePorEntidad));

        return ranking;
    }

    public Map<Entidad, Double> generarPromedioCierrePorEntidad(Map<Entidad, List<Incidente>> incidentesPorEntidad) {

        Map<Entidad, Double> promedioCierrePorEntidad = new HashMap<>();
        for (Map.Entry<Entidad, List<Incidente>> entry : incidentesPorEntidad.entrySet()) {
            Entidad entidad = entry.getKey();
            List<Incidente> incidentes = entry.getValue();

            if (!incidentes.isEmpty()) {
                long sumaTiempoDeCierre = incidentes.stream()
                        .mapToLong(Incidente::obtenerTiempoDeCierre)
                        .sum();
                int cantIncidentes = incidentes.size();

                // Validación para evitar la división por cero
                if (cantIncidentes != 0) {
                    promedioCierrePorEntidad.put(entidad, (double) sumaTiempoDeCierre / cantIncidentes);
                    entidad.setPromedioCierre(promedioCierrePorEntidad.get(entidad));
                }
            }
        }
        return promedioCierrePorEntidad;
    }
}
