package models.entities.domain.registro.condicionesContra.medidorFuerza;


import models.entities.domain.registro.Contrasenia;

public abstract class MedidorDeFuerza {

    public String fuerza(Contrasenia contrasenia) {

        String className = this.getClass().getSimpleName();
        return className;

    }


}
