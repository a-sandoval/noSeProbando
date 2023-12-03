package models.entities.domain.registro.condicionesContra;

import models.entities.domain.config.Config;
import models.entities.domain.registro.Contrasenia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class UsoReiterado implements Condicion{
    @Override
    public boolean cumpleCondicion(Contrasenia contrasenia, String nombreUsuario) {
        try {
            return !this.esMuyUsada(contrasenia);
        } catch (ContraseniaUsoReiteradoException e) {
            throw new ContraseniaUsoReiteradoException("La contraseña ha sido utilizada anteriormente y es considerada débil. Por favor, elige una contraseña diferente.");
        }
    }

    public boolean esMuyUsada(Contrasenia contrasenia) {
        File archivoContrasenias = new File(Config.RUTA_ARCHIVOS + Config.PEORES_CONTRASEÑAS_TXT);
        try {
            if (archivoContrasenias.exists()) {
                BufferedReader leerArchivo = new BufferedReader(new FileReader(archivoContrasenias));
                String lineaLeida;
                while ((lineaLeida = leerArchivo.readLine()) != null) {
                    String[] contraseniasUsadas = lineaLeida.split(" ");
                    for (String contraseniaUsada : contraseniasUsadas) {
                        if (contraseniaUsada.equals(contrasenia.getContrasenia())) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }


}
