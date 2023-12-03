package controllers;

import io.javalin.http.Context;
import models.entities.domain.registro.Usuario;
import models.repositories.datos.RepositorioEntidades;
import server.exceptions.AccessDeniedException;
import server.utils.ICrudViewsHandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankingController extends Controller implements ICrudViewsHandler {


    @Override
    public void index(Context context) {
    }

    @Override
    public void show(Context context) {

        Usuario usuarioLogueado = super.usuarioLogueado(context);

        if(usuarioLogueado == null || !usuarioLogueado.tenesPermiso("ver_rankings_entidades")) {
            throw new AccessDeniedException();
        }

        context.render("presentacion/rankings.hbs");

    }
    @Override
    public void create(Context context) {

    }

    @Override
    public void save(Context context) {

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

    public void rankingReportes(Context context){
            Path pathAlArchivo = Paths.get("src/main/java/models/repositories/datos/rankingReportes.csv");
            if (Files.exists(pathAlArchivo)) {
                try {
                    context.result(new FileInputStream(pathAlArchivo.toFile()));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                context.header("Content-Disposition", "attachment; filename=rankingReportes.csv");
                try {
                    context.contentType(Files.probeContentType(pathAlArchivo));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                context.status(404).result("File not found");
            }
    }


    public void rankingImpactoIncidentes(Context context){
        Path pathAlArchivo = Paths.get("src/main/java/models/repositories/datos/rankingImpactoIncidentes.csv");
        if (Files.exists(pathAlArchivo)) {
            try {
                context.result(new FileInputStream(pathAlArchivo.toFile()));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            context.header("Content-Disposition", "attachment; filename=rankingReportes.csv");
            try {
                context.contentType(Files.probeContentType(pathAlArchivo));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            context.status(404).result("File not found");
        }
    }
    public void rankingPromedioCierre(Context context){
        Path pathAlArchivo = Paths.get("src/main/java/models/repositories/datos/rankingPromedioCierreu.csv");
        if (Files.exists(pathAlArchivo)) {
            try {
                context.result(new FileInputStream(pathAlArchivo.toFile()));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            context.header("Content-Disposition", "attachment; filename=rankingReportes.csv");
            try {
                context.contentType(Files.probeContentType(pathAlArchivo));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            context.status(404).result("File not found");
        }
    }
}
