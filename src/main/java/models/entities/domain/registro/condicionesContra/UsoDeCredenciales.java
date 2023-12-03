package models.entities.domain.registro.condicionesContra;

import models.entities.domain.registro.Contrasenia;

import java.util.Objects;

public class UsoDeCredenciales implements Condicion {

    @Override
    public boolean cumpleCondicion(Contrasenia contrasenia, String nombreUsuario) {
        if(this.utilizaCredencialesPorDefecto(contrasenia, nombreUsuario))
            throw new ContraseniaUtilizaCredencialesPorDefectoException("La contraseña no puede ser igual al nombre de usuario.");
        else
            return true;
    }

    public boolean utilizaCredencialesPorDefecto(Contrasenia contrasenia, String nombreUsuario) {
        System.out.println(contrasenia.getContrasenia());
        System.out.print(nombreUsuario);
        return nombreUsuario.equals(contrasenia.getContrasenia());
    }

}
