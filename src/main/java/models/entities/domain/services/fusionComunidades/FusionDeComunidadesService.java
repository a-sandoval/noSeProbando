package models.entities.domain.services.fusionComunidades;

import models.entities.domain.comunidad.Comunidad;
import models.entities.domain.services.fusionComunidades.entities.PeticionDeFusion;
import models.entities.domain.services.fusionComunidades.entities.PropuestaDeFusion;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

import java.util.ArrayList;

public interface FusionDeComunidadesService {

    @GET("/comunidad/sugerirFusiones")
    Call<ArrayList<PeticionDeFusion>> sugerirFusiones(@Body PeticionDeFusion peticion);

    @GET("/comunidad/fusionarComunidades")
    Call<Comunidad> fusionarComunidades(@Body PropuestaDeFusion propuesta);

    //poner en inactivo las comunidades que se fusionaron corre de nuestro lado
}
