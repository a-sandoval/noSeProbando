package models.entities.domain.incidentes.rankings;

import models.entities.domain.entidades.Entidad;
import models.entities.domain.incidentes.Incidente;

import java.util.ArrayList;

public abstract class Ranking {

    Filtrador filtradorDeIncidentes = new Filtrador();
    ArrayList<Incidente> incidentesSemanales = filtradorDeIncidentes.filtrarIncidentesUltimaSemana();

    public abstract ArrayList<Entidad> generarRanking();
}
