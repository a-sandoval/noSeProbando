package models.entities.domain.services.georef;

import models.entities.domain.services.georef.entities.RespuestaAPI;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeorefService {

    @GET("ubicacion")
    Call<RespuestaAPI> obtenerUbicacion(@Query("lat") double latitud, @Query("lon") double longitud);
}
