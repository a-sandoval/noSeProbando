package models.entities.domain.services.fusionComunidades.entities;

import lombok.Getter;
import lombok.Setter;
import models.entities.domain.comunidad.Comunidad;

import java.util.ArrayList;

@Getter
@Setter
public class PeticionDeFusion {

    private ArrayList<PropuestaDeFusion> propuestas;
    private ArrayList<Comunidad> comunidades;


}
