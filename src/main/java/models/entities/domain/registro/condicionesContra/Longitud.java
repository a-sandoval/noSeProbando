package models.entities.domain.registro.condicionesContra;

import models.entities.domain.registro.Contrasenia;

public class Longitud implements Condicion {
    @Override
    public boolean cumpleCondicion(Contrasenia contrasenia, String nombreUsuario) throws ContraseniaNoCumpleConLongitudException {
        return this.cumpleConLongitud(contrasenia);
    }

    public boolean excedeCaracteres(Contrasenia contrasenia) {
        return contrasenia.getContrasenia().length() >= 64;
    }

    public boolean noAlcanzaCaracteres(Contrasenia contrasenia) {
        return contrasenia.getContrasenia().length() < 8;
    }

    public boolean cumpleConLongitud(Contrasenia contrasenia) throws ContraseniaNoCumpleConLongitudException {
        if ((!excedeCaracteres(contrasenia)) && (!noAlcanzaCaracteres(contrasenia))) {
            return true;
        } else {
            throw new ContraseniaNoCumpleConLongitudException("La contrasenia no cumple con la longitud necesaria");
        }
    }
}
