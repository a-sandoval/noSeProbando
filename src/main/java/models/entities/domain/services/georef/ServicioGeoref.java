package models.entities.domain.services.georef;

import models.entities.domain.config.Config;
import models.entities.domain.services.georef.entities.RespuestaAPI;
import models.entities.domain.services.georef.entities.Ubicacion;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ServicioGeoref {
    private static ServicioGeoref instancia = null;
    private static final String urlAPI = Config.URL_APIGEOREF;
    private Retrofit retrofit;

    private ServicioGeoref(){
        this.retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static ServicioGeoref getInstancia(){
        if(instancia ==null) {
            instancia = new ServicioGeoref();
        }
        return instancia;
    }

    public Ubicacion obtenerDetallesUbicacion(double lat, double lon) throws IOException {

        GeorefService georefService = this.retrofit.create(GeorefService.class);
        Call<RespuestaAPI> peticion = georefService.obtenerUbicacion(lat, lon);
        Response<RespuestaAPI> respuesta = peticion.execute();

        if (respuesta.isSuccessful()) {
            Ubicacion ubicacion = respuesta.body().getUbicacion();
            return ubicacion;
        } else {
            // Manejar el error si la respuesta no es exitosa
            throw new IOException("Error en la respuesta: " + respuesta.message());
        }

    }
}
