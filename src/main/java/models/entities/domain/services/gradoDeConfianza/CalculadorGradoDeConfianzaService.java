package models.entities.domain.services.gradoDeConfianza;

import models.entities.domain.services.gradoDeConfianza.entities.GradoDeConfianzaComunidad;
import models.entities.domain.services.gradoDeConfianza.entities.GradoDeConfianzaDeUsuario;
import models.entities.domain.services.gradoDeConfianza.entities.requests.ComunidadRequest;
import models.entities.domain.services.gradoDeConfianza.entities.requests.UsuarioRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CalculadorGradoDeConfianzaService {

    @POST("gradoDeConfianza/usuario")
    Call<GradoDeConfianzaDeUsuario> obtenerGradoDeConfianzaActual(@Body UsuarioRequest usuarioRequest);

    @POST("gradoDeConfianza/comunidad")
    Call<GradoDeConfianzaComunidad> obtenerGradoDeComunidad(@Body ComunidadRequest comunidadRequest);
}
