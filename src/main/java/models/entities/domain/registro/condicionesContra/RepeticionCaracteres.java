package models.entities.domain.registro.condicionesContra;

import models.entities.domain.registro.Contrasenia;

public class RepeticionCaracteres implements Condicion{
    @Override
    public boolean cumpleCondicion(Contrasenia contrasenia, String nombreUsuario) {
        if (!this.noRepiteCaracteres(contrasenia)) {
        throw new ContraseniaRepiteCaracteresException("La contraseña repite muchos caracteres");
    }
        return true;
    }

    public boolean noRepiteCaracteres(Contrasenia contrasenia)
    {
        if(! this.tieneCaracteresRepetidosXVeces(contrasenia.getContrasenia(), 3))
        {
            return true;
        }
        else
        {
            throw new ContraseniaRepiteCaracteresException("La contraseña repite muchos caracteres");
        }
    }

    public boolean tieneCaracteresRepetidosXVeces(String contrasenia, int nroRepes)
    {
           boolean hayRepetido = false;

           for (int i = 0; i < contrasenia.length(); i++)
           {
               int contadorRepeticiones = 0;
               for (int j = 0; j <= nroRepes; j++)
               {
                   if (contrasenia.charAt(i) == contrasenia.charAt(j))
                   {
                       contadorRepeticiones++;
                   }
               }
               if (contadorRepeticiones >= nroRepes)
               {
                   hayRepetido = true;
                   break;
               }
           }
           return hayRepetido;
    }
}


