package models.entities.domain.services.fusionComunidades;

import models.entities.domain.config.Config;
import models.entities.domain.services.fusionComunidades.entities.PeticionDeFusion;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;

public class FusionDeComunidadesAPI {
    private static FusionDeComunidadesAPI instancia = null;
    private static final String urlAPI = Config.URL_API_SUGERIR_FUSION_COMUNIDADES;
    private Retrofit retrofit;

    private FusionDeComunidadesAPI() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static FusionDeComunidadesAPI getInstancia(){
        if(instancia ==null) {
            instancia = new FusionDeComunidadesAPI();
        }
        return instancia;
    }

    public ArrayList<PeticionDeFusion> sugerirFusiones(PeticionDeFusion peticion) throws IOException, InstantiationException, IllegalAccessException {
        FusionDeComunidadesService fusion = this.retrofit.create(FusionDeComunidadesService.class);
        Call<ArrayList<PeticionDeFusion>> llamado = fusion.sugerirFusiones(peticion);
        Response<ArrayList<PeticionDeFusion>> respuesta = llamado.execute();

        if (respuesta.isSuccessful()){
            ArrayList<PeticionDeFusion> peticionActualizada = respuesta.body();
            return peticionActualizada;
        }else {
            throw new IOException("Error en la respuesta: " + respuesta.message());
        }

    }
}

