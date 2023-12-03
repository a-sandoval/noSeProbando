package models.entities.domain.registro;

import models.entities.domain.registro.condicionesContra.*;

import java.util.ArrayList;
import java.util.List;

public class Validador {

    private ArrayList<Condicion> validador = new ArrayList<>();

    public Validador() {

        this.agregarCondiciones(new Longitud(), new RepeticionCaracteres(), new UsoDeCredenciales(), new UsoReiterado());

    }

    public void agregarCondiciones(Condicion... condicion) {

        this.validador.addAll(List.of(condicion));

    }

    public boolean esValida(Contrasenia contrasenia, String nombreUsuario) {
        return this.validador.stream().allMatch(condicion->condicion.cumpleCondicion(contrasenia, nombreUsuario));
    }

}
