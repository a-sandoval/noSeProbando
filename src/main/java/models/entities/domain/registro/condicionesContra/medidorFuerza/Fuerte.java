package models.entities.domain.registro.condicionesContra.medidorFuerza;

import models.entities.domain.registro.Contrasenia;

public class Fuerte extends MedidorDeFuerza{

    // Para que sea fuerte tiene > 10 caracteres, incluye números, letras May y minus y tiene al menos 1 caracter especial

    @Override
    public String fuerza(Contrasenia contrasenia){

        return super.fuerza(contrasenia);
    }
}
