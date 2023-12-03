package models.entities.domain.incidentes.rankings;

import models.entities.domain.entidades.Entidad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class CantidadDeIncidentes extends Ranking {

    public ArrayList<Entidad> generarRanking() {

        Map<Entidad, Double> conteoEntidades = new HashMap<>();

        ArrayList<Entidad> entidades = filtradorDeIncidentes.filtrarRepetidosEn24hs(incidentesSemanales).stream().map(incidente -> incidente.obtenerEntidad()).collect(Collectors.toCollection(ArrayList::new));
        if (incidentesSemanales.isEmpty()) {
            throw new IllegalStateException("No hay incidentes en la última semana."); // Lanzar una excepción
        }
        for (Entidad entidad : entidades) {
        conteoEntidades.put(entidad, conteoEntidades.getOrDefault(entidad, 0.0) + 1);

        if(conteoEntidades.get(entidad)!=0) {
        entidad.setCantidadReportes(conteoEntidades.get(entidad).intValue());
        }
        }

        ArrayList<Entidad> ranking = new ArrayList<>(new HashSet<>(entidades));
        ranking.sort(new RepeticionEntidadesComparador(conteoEntidades));

        return ranking;

        }
        }
