package models.entities.domain.incidentes.rankings;

import models.entities.domain.entidades.Entidad;

import java.util.Comparator;
import java.util.Map;

class RepeticionEntidadesComparador implements Comparator<Entidad> {
    private final Map<Entidad, Double> conteoEntidades;

    public RepeticionEntidadesComparador(Map<Entidad, Double> conteoEntidades){
        this.conteoEntidades = conteoEntidades;
    }

    @Override
    public int compare(Entidad entidad1, Entidad entidad2){
        double rep1 = conteoEntidades.get(entidad1);
        double rep2 = conteoEntidades.get(entidad2);

        return Double.compare(rep2, rep1);
    }
}
