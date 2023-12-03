package server;



import controllers.*;
import jakarta.servlet.http.HttpSession;
import models.entities.domain.roles.TipoRol;
import server.exceptions.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import static io.javalin.apibuilder.ApiBuilder.*;

public class Router {

    public static void init() {
        // index
        Server.app().before("/entidades", ctx -> {

            boolean usuarioRegistrado = ctx.sessionAttribute("id") != null;

            if (!usuarioRegistrado) {
                throw new AccessDeniedException();
            }
        });

        Server.app().routes(() -> {


            get("/", ctx -> {
                ctx.header("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
                ctx.header("Pragma", "no-cache");
                ctx.header("Expires", "0");
                ctx.render("index/inicioSesion.hbs");
            });
            post("/", ctx -> {

                // Lógica de autenticación, por ejemplo, verificar el usuario y contraseña
                String username = ctx.formParam("usuario");
                String password = ctx.formParam("contrasenia");

                Long id = retornarIDSiCredencialesSonCorrectas(username, password);
                // Si las credenciales son válidas, establece una sesión para el usuario
                if (id != null) {
                    ctx.sessionAttribute("authenticated", true);
                    ctx.sessionAttribute("id", id);
                    ctx.redirect("/home");// Redirige al usuario a la página de inicio
                } else {
                    String error = "Credenciales incorrectas";
                    Map<String, Object> model = new HashMap<>();
                    model.put("error", error);
                    ctx.render("index/inicioSesion.hbs", model);
                }

            });
            get("signup", ((UsuarioController) FactoryController.controller("Usuario"))::create);
            post("signup", ((UsuarioController) FactoryController.controller("Usuario"))::save);

            get("/entidades", ((EntidadController) FactoryController.controller("Entidad"))::index);
            post("/entidades", ((UsuarioController) FactoryController.controller("Usuario"))::updateEntities);

            get("/logout", ctx -> {
                ctx.removeCookie("JSESSIONID");
                ctx.header("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
                ctx.header("Pragma", "no-cache");
                ctx.header("Expires", "0");
                ctx.sessionAttribute("authenticated", null);
                ctx.sessionAttribute("id", null);
                ctx.redirect("/");
            });
        });

        Server.app().routes(() -> {
            get("rankings", ((RankingController) FactoryController.controller("Ranking"))::show);
            get("rankings/rankingReportes", ((RankingController) FactoryController.controller("Ranking"))::rankingReportes);
            get("rankings/rankingImpactoIncidentes", ((RankingController) FactoryController.controller("Ranking"))::rankingImpactoIncidentes);
            get("rankings/rankingPromedioCierre", ((RankingController) FactoryController.controller("Ranking"))::rankingPromedioCierre);
        });

        Server.app().routes(() -> {
            get("/comunidades", ((ComunidadController) FactoryController.controller("Comunidad"))::index);
            get("/comunidades/crear", ((ComunidadController) FactoryController.controller("Comunidad"))::create);
            post("/comunidades/crear", ((ComunidadController) FactoryController.controller("Comunidad"))::save);
            get("/comunidades/unirse", ((ComunidadController) FactoryController.controller("Comunidad"))::show);
            get("/comunidades/{id}", ((ComunidadController) FactoryController.controller("Comunidad"))::showById);
            post("/comunidades/unirse", ((UsuarioController) FactoryController.controller("Usuario"))::joinCommunity);
            post("/comunidades/{id}/salir" , ((UsuarioController) FactoryController.controller("Usuario"))::leaveComunity);

        });

        Server.app().routes(() -> {
            get("home", ((UsuarioController) FactoryController.controller("Usuario"))::index);
            get("editar", ((UsuarioController) FactoryController.controller("Usuario"))::edit);
            post("editar", ((UsuarioController) FactoryController.controller("Usuario"))::update);
        });

        Server.app().routes(() -> {
            get("incidentes/crear", ((IncidenteController) FactoryController.controller("Incidente"))::create);
            post("incidentes/crear", ((IncidenteController) FactoryController.controller("Incidente"))::save);
            get("incidentes/{id}/editar", ((IncidenteController) FactoryController.controller("Incidente"))::edit);
            post("incidentes/{id}/editar", ((IncidenteController) FactoryController.controller("Incidente"))::update);
            get("incidentes/sugerencias", ((IncidenteController) FactoryController.controller("Incidente")) :: suggest);
            get("incidentes/ubicaciones", ((IncidenteController) FactoryController.controller("Incidente")) :: api);

        });

        Server.app().routes(() -> {
            get("cargadatos", ((CargaOrganismosYEntidadesController) FactoryController.controller("CargaOrganismosYEntidadesController"))::index);
            post("cargadatos", ((CargaOrganismosYEntidadesController) FactoryController.controller("CargaOrganismosYEntidadesController"))::save);
        });


    }

    private static Long retornarIDSiCredencialesSonCorrectas(String username, String password) {

        UsuarioController controller = (UsuarioController) FactoryController.controller("Usuario");

        Long id = controller.retornarIdSiExiste(username);

        if(controller.esCorrecta(username, password)) {
            return id; 
        }
       else  {
            return null;
            //todo handle exception de flaco tirame bien la contra
        }

    }

    private static Long retornarIDPostRegistro(String username) {

        UsuarioController controller = (UsuarioController) FactoryController.controller("Usuario");

        return controller.retornarIdSiExiste(username);

    }
}