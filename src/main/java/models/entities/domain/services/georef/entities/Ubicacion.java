package models.entities.domain.services.georef.entities;

import models.entities.domain.config.Config;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Transient;

@Getter
@Setter
public class Ubicacion {
    @Column
    private double lat;
    @Column
    private double lon;

    @Transient
    private Departamento departamento;
    @Transient
    private Municipio municipio;
    @Transient
    private Provincia provincia;

    public Ubicacion(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Ubicacion() {}

    public Boolean estasCercaDe(Ubicacion ubi){

        double RADIO_TIERRA_KM = Config.RADIO_TIERRA_KM;
        // Convertir las coordenadas a radianes
        double lat1Rad = Math.toRadians(this.getLat());
        double lon1Rad = Math.toRadians(this.getLon());
        double lat2Rad = Math.toRadians(ubi.getLat());
        double lon2Rad = Math.toRadians(ubi.getLon());

        // Calcular la diferencia entre las coordenadas
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        // Aplicar la fórmula de haversine
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calcular la distancia en kilómetros
        double distanciaKm = RADIO_TIERRA_KM * c;

        return distanciaKm < Config.DISTANCIA_PERMITIDA_PARA_INCIDENTES;

    }
}
