package controllers;

import com.opencsv.exceptions.CsvValidationException;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.UploadedFile;
import models.entities.domain.entidades.Entidad;
import models.entities.domain.entidades.cargaEntidadesyOrgDeControl;
import models.entities.domain.incidentes.Incidente;
import models.entities.domain.registro.Usuario;
import models.repositories.datos.RepositorioEntidades;
import org.apache.commons.io.FileUtils;
import server.exceptions.AccessDeniedException;
import server.utils.ICrudViewsHandler;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;

public class CargaOrganismosYEntidadesController extends Controller implements ICrudViewsHandler {

    @Override
    public void index(Context context) {
        Usuario usuarioLogueado = super.usuarioLogueado(context);
        if(usuarioLogueado == null || !usuarioLogueado.tenesPermiso("cargar_csv")) {
            throw new AccessDeniedException();
        }
        context.render("admin/cargaMasivaDeDatos.hbs");
    }

    @Override
    public void show(Context context) {


    }

    @Override
    public void create(Context context) {

    }

    @Override
    public void save(Context context) {

        Usuario usuarioLogueado = super.usuarioLogueado(context);
        cargaEntidadesyOrgDeControl cargador = new cargaEntidadesyOrgDeControl();

        if(usuarioLogueado == null || !usuarioLogueado.tenesPermiso("cargar_csv")) {
            throw new AccessDeniedException();
        }
        context.uploadedFiles("archivos").forEach(file -> {
            try {
                File nuevoArchivo = new File("src/main/resources/subidas" + file.filename());
                FileUtils.copyInputStreamToFile(file.content(), nuevoArchivo);
                cargador.cargarEntidadesYOrgDeControl(nuevoArchivo.getAbsolutePath());


            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (CsvValidationException e) {
                throw new RuntimeException(e);
            }
        });

        context.redirect("home");


    }

    @Override
    public void edit(Context context) {

    }

    @Override
    public void update(Context context) {

    }

    @Override
    public void delete(Context context) {

    }
}
