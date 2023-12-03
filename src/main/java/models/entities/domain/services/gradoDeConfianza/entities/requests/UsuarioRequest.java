package models.entities.domain.services.gradoDeConfianza.entities.requests;

import models.entities.domain.incidentes.Incidente;
import models.entities.domain.registro.Usuario;

import java.util.List;

public class UsuarioRequest {

    public Usuario usuario;

    public List<Incidente> incidentes;
}
