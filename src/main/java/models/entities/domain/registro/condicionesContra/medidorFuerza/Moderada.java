package models.entities.domain.registro.condicionesContra.medidorFuerza;

import models.entities.domain.registro.Contrasenia;

public class Moderada extends MedidorDeFuerza{

    // Para que sea moderada tiene > 10 caracteres, incluye números y letras May y minus

    @Override
    public String fuerza(Contrasenia contrasenia) {

        if (this.cumpleCondicionesParaFuerte(contrasenia)) {
            contrasenia.setFuerza(new Fuerte());
        }
        return super.fuerza(contrasenia);
   }

    private boolean cumpleCondicionesParaFuerte(Contrasenia contrasenia) {
        return (contrasenia.getContrasenia().length() > 10 &&
                contrasenia.tieneNum() &&
                contrasenia.tieneMayusyMinus() &&
                contrasenia.tieneCaracterEspecial());
    }
}
