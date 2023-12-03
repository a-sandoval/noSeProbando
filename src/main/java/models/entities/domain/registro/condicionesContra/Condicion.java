package models.entities.domain.registro.condicionesContra;

import models.entities.domain.registro.Contrasenia;

public interface Condicion {

    boolean cumpleCondicion (Contrasenia contrasenia, String nombreUsuario);

}
