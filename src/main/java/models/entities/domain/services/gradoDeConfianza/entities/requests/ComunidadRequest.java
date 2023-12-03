package models.entities.domain.services.gradoDeConfianza.entities.requests;

import models.entities.domain.comunidad.Comunidad;
import models.entities.domain.incidentes.Incidente;

import java.util.List;

public class ComunidadRequest {

    public Comunidad comunidad;

    public List<Incidente> incidentes;

    public ComunidadRequest(Comunidad comunidad, List<Incidente> incidentes) {
        this.comunidad = comunidad;
        this.incidentes = incidentes;
    }
}
