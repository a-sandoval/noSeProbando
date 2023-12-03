package models.entities.domain.services.gradoDeConfianza;

import models.entities.domain.config.Config;
import models.entities.domain.services.georef.GeorefService;
import models.entities.domain.services.georef.entities.RespuestaAPI;
import models.entities.domain.services.georef.entities.Ubicacion;
import models.entities.domain.services.gradoDeConfianza.entities.GradoDeConfianzaComunidad;
import models.entities.domain.services.gradoDeConfianza.entities.GradoDeConfianzaDeUsuario;
import models.entities.domain.services.gradoDeConfianza.entities.requests.ComunidadRequest;
import models.entities.domain.services.gradoDeConfianza.entities.requests.UsuarioRequest;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class GradoDeConfianzaAPI {
    private static GradoDeConfianzaAPI instancia = null;
    private static final String urlAPI = Config.URL_APIGEOREF;
    private Retrofit retrofit;

    public GradoDeConfianzaAPI() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public GradoDeConfianzaComunidad obtenerGradoDeConfianzaPara(ComunidadRequest comunidadRequest) throws IOException {

        CalculadorGradoDeConfianzaService calculadorService = this.retrofit.create(CalculadorGradoDeConfianzaService.class);
        Call<GradoDeConfianzaComunidad> peticion = calculadorService.obtenerGradoDeComunidad(comunidadRequest);
        Response<GradoDeConfianzaComunidad> respuesta = peticion.execute();

        if (respuesta.isSuccessful()) {
           GradoDeConfianzaComunidad gradoDeConfianzaComunidad = respuesta.body();
            return gradoDeConfianzaComunidad;
        } else {
            // Manejar el error si la respuesta no es exitosa
            throw new IOException("Error en la respuesta: " + respuesta.message());
        }

    }

    public GradoDeConfianzaDeUsuario obtenerGradoDeConfianzaPara(UsuarioRequest usuarioRequest)throws IOException {

        CalculadorGradoDeConfianzaService calculadorService = this.retrofit.create(CalculadorGradoDeConfianzaService.class);
        Call<GradoDeConfianzaDeUsuario> peticion = calculadorService.obtenerGradoDeConfianzaActual(usuarioRequest);
        Response<GradoDeConfianzaDeUsuario> respuesta = peticion.execute();

        if (respuesta.isSuccessful()) {
            GradoDeConfianzaDeUsuario gradoDeConfianzaDeUsuario = respuesta.body();
            return gradoDeConfianzaDeUsuario;
        } else {
            // Manejar el error si la respuesta no es exitosa
            throw new IOException("Error en la respuesta: " + respuesta.message());
        }
    }

}
