package models.entities.domain.services.fusionComunidades.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.SerializedName;
import models.entities.domain.comunidad.Comunidad;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PropuestaDeFusion {

    private ArrayList<Comunidad> comunidadesAFusionar = new ArrayList<>();
    @SerializedName("fechaPropuesta")
    private String fechaPropuesta;

    public PropuestaDeFusion() {
        this.fechaPropuesta = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public Boolean masDeXMesesDePropuesta(Integer mes){
        return LocalDateTime.parse(fechaPropuesta, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .plusMonths(mes)
                .isBefore(LocalDateTime.now());
    }

    public void agregarComunidad(Comunidad comunidadModel){
        comunidadesAFusionar.add(comunidadModel);
    }
}
