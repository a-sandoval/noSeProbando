package models.entities.domain.services.georef.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Municipio{
    public int id;
    public String nombre;
    public Provincia provincia;
}
