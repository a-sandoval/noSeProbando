package controllers;

import io.javalin.http.HttpStatus;
import models.entities.domain.comunidad.Comunidad;
import models.entities.domain.entidades.Entidad;
import models.entities.domain.incidentes.Incidente;
import models.entities.domain.notificaciones.tiempoDeEnvio.ModoRecepcion;
import models.entities.domain.registro.Contrasenia;
import models.entities.domain.registro.Encriptador;
import models.entities.domain.registro.Validador;
import models.entities.domain.registro.condicionesContra.ContraseniaNoCumpleConLongitudException;
import models.entities.domain.registro.condicionesContra.ContraseniaRepiteCaracteresException;
import models.entities.domain.registro.condicionesContra.ContraseniaUsoReiteradoException;
import models.entities.domain.registro.condicionesContra.ContraseniaUtilizaCredencialesPorDefectoException;
import models.entities.domain.roles.Rol;
import models.repositories.datos.RepositorioComunidades;
import models.repositories.datos.RepositorioDeRoles;
import models.repositories.datos.RepositorioEntidades;
import models.repositories.datos.RepositorioUsuarios;
import server.utils.ICrudViewsHandler;
import models.entities.domain.registro.Usuario;
import io.javalin.http.Context;
import server.utils.Initializer;

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class UsuarioController extends Controller implements ICrudViewsHandler {
     private RepositorioUsuarios repositorioUsuarios;
     private RepositorioEntidades repositorioEntidades;

     public UsuarioController(RepositorioUsuarios repositorioDeUsuarios, RepositorioEntidades repositorioEntidades) {
          this.repositorioUsuarios = repositorioDeUsuarios;
          this.repositorioEntidades = repositorioEntidades;
     }

     @Override
     public void index(Context context) {
         IncidenteController incidenteController = (IncidenteController) FactoryController.controller("Incidente");
         incidenteController.index(context);
     }

     @Override
     public void show(Context context) {

     }

     @Override
     public void create(Context context) {
          ArrayList<String> medios = (ArrayList<String>) Initializer.obtenerMedioDeNotificacionValidos();
          ArrayList<String> modos = (ArrayList<String>) Initializer.obtenerModosDeRecepcionValidos();
          ArrayList<LocalTime> horarios = (ArrayList<LocalTime>) Initializer.obtenerHorarios();

          Map<String, Object> model = new HashMap<>();
          model.put("medios", medios);
          model.put("modos", modos);
          model.put("horarios", horarios);

          context.render("index/registro.hbs", model);

     }

     @Override
     public void save(Context context) {

          Usuario usuario= new Usuario();
          //sacar datos del form del contexto
          this.asignarParametros(usuario, context);

          String errorMessage = "";
          Map<String, Object> model = new HashMap<>();

          try {
               Validador validador = new Validador();
               Contrasenia contrasenia = new Contrasenia(context.formParam("contrasenia"));
               String nombreUsuario = context.formParam(("nombre"));
               validador.esValida(contrasenia, nombreUsuario);
          repositorioUsuarios.agregarUsuario(usuario);
          context.status(HttpStatus.CREATED);
          context.sessionAttribute("authenticated", true);
          context.sessionAttribute("id", retornarIdSiExiste(usuario.getNombreDeUsuario()));
          context.redirect("/entidades"); // para que cargue sus entidades de interes
          } catch (ContraseniaUsoReiteradoException e) {
               errorMessage = "La contraseña ingresada ya ha sido utilizada anteriormente. Por favor, elige una contraseña diferente.";

          } catch (ContraseniaUtilizaCredencialesPorDefectoException e) {
               errorMessage = "La contraseña no puede ser su credencial. Por favor, elige una contraseña diferente.";

          } catch (ContraseniaRepiteCaracteresException e) {
              errorMessage = "La contraseña no puede repetir caracteres. Por favor, elige una contraseña diferente.";

          } catch (ContraseniaNoCumpleConLongitudException e) {
               errorMessage = "La contraseña ingresada no cumple con la longitud necesaria. Por favor, elige una contraseña diferente.";

          } finally {
               if(errorMessage !="") {
                    ArrayList<String> medios = (ArrayList<String>) Initializer.obtenerMedioDeNotificacionValidos();
                    ArrayList<String> modos = (ArrayList<String>) Initializer.obtenerModosDeRecepcionValidos();
                    ArrayList<LocalTime> horarios = (ArrayList<LocalTime>) Initializer.obtenerHorarios();
                    model.put("errorMessage", errorMessage);
                    model.put("medios", medios);
                    model.put("modos", modos);
                    model.put("horarios", horarios);
                    context.render("index/registro.hbs", model);
               }
          }
     }

     @Override
     public void edit(Context context) {
          String id = Objects.requireNonNull(context.sessionAttribute("id")).toString();
          Usuario usuario = (Usuario) this.repositorioUsuarios.buscar(Long.parseLong(id));
          Map<String, Object> model = new HashMap<>();
          model.put("usuario", usuario);

          ArrayList<String> medios = (ArrayList<String>) Initializer.obtenerMedioDeNotificacionValidos();
          ArrayList<String> modos = (ArrayList<String>) Initializer.obtenerModosDeRecepcionValidos();
          ArrayList<LocalTime> horarios = (ArrayList<LocalTime>) Initializer.obtenerHorarios();

          model.put("medios", medios);
          model.put("modos", modos);
          model.put("horarios", horarios);

          context.render("presentacion/editarPerfil.hbs", model);

     }

     @Override
     public void update(Context context) {
          Usuario usuario = (Usuario) this.repositorioUsuarios.buscar(Long.parseLong(context.sessionAttribute("id").toString()));
          this.asignarParametros(usuario, context);
          this.repositorioUsuarios.actualizar(usuario);
          context.redirect("/home");
     }

     public void updateEntities(Context context) {
          String id = Objects.requireNonNull(context.sessionAttribute("id")).toString();
          Usuario usuario = (Usuario) this.repositorioUsuarios.buscar(Long.parseLong(id));

          List<String> entidades = context.formParams("entidades");

          for(String nombre:entidades) {

              usuario.agregarEntidadesDeInteres(this.repositorioEntidades.filtrarPorNombre(nombre));

          }
          this.repositorioUsuarios.actualizar(usuario); 
          context.redirect("/home");

     }

     @Override
     public void delete(Context context) {
         Usuario usuario = (Usuario) this.repositorioUsuarios.buscar(Long.parseLong(context.pathParam("id")));
         usuario.setBloqueado(true);
         this.repositorioUsuarios.actualizar(usuario);
         context.redirect("/");
     }

     public void  joinCommunity(Context context){

          String id = Objects.requireNonNull(context.sessionAttribute("id")).toString();
          Usuario usuario = (Usuario) this.repositorioUsuarios.buscar(Long.parseLong(id));

          String opcionComunidad = context.formParam("comunidades");

          Comunidad aUnirse = (Comunidad) new RepositorioComunidades().filtrarPorNombre(opcionComunidad).get(0);

          RolPermisoController controller = (RolPermisoController)FactoryController.controller("RolPermiso");

          Rol nuevoRol = controller.asignarPermisosComoMiembroDeComunidad(aUnirse);

          new RepositorioDeRoles().guardar(nuevoRol);

          usuario.aniadirRol(nuevoRol);

          repositorioUsuarios.actualizar(usuario);

          aUnirse.agregarUsuarios(usuario);

          new RepositorioComunidades().actualizar(aUnirse);

          context.status(HttpStatus.OK);

          context.redirect("/comunidades");

     }
     
     public void leaveComunity(Context context){
          String id = Objects.requireNonNull(context.sessionAttribute("id")).toString();
          Usuario usuario = (Usuario) this.repositorioUsuarios.buscar(Long.parseLong(id));

          String comunidadDeDondeSalir = context.pathParam("id");
          Comunidad aSalir = (Comunidad) new RepositorioComunidades().buscar(Long.parseLong(comunidadDeDondeSalir));

          List<Rol> rolesDeUsuario = usuario.getRoles();
          Rol rolDeUsuario = null;

          for (Rol rol : rolesDeUsuario) {
              if (rol.getComunidad() != null && rol.getComunidad().equals(aSalir)){
                   rolDeUsuario = rol;
                   break;
              }
          }

          if(rolDeUsuario !=null) {

               new RepositorioDeRoles().remove(rolDeUsuario);
               usuario.quitarRol(rolDeUsuario);

          }
          new RepositorioComunidades().eliminarUsuario(aSalir.getId(),Long.parseLong(id));
          new RepositorioComunidades().actualizar(aSalir);
          new RepositorioUsuarios().actualizar(usuario);
          context.status(HttpStatus.OK);
          context.redirect("/comunidades");


     }

     public void addServices(Context context) {

     }

     public Long retornarIdSiExiste(String username){

          List<Usuario> usuarios = repositorioUsuarios.filtrarPorNombre(username);

          if(!usuarios.isEmpty()) {
               return usuarios.get(0).getId();
          }

          else
               return null;

     }

     private void asignarParametros(Usuario usuario, Context context) {
          if(!Objects.equals(context.formParam("nombre"), "")) {
               usuario.setNombreDeUsuario(context.formParam("nombre"));
          }
          if(!Objects.equals(context.formParam("contrasenia"), "")) {
               usuario.setContra(context.formParam("contrasenia"));
          }
          if(!Objects.equals(context.formParam("email"), "")) {
               usuario.setEmail(context.formParam("email"));
          }
          if(!Objects.equals(context.formParam("telefono"), "")) {
               usuario.setNumeroTelefono(context.formParam("telefono"));
          }

          String medio = context.formParam("medios");

          if(medio!=null) {
               usuario.setMedioDeNotificacion(medio);
          }

          String modo = context.formParam("modos");

          if(modo!=null) {
               usuario.setModoRecepcion(ModoRecepcion.valueOf(modo.toLowerCase()));
          }

          if(context.formParams("horarios")!=null) {
               List<String> horariosSeleccionados = context.formParams("horarios");
               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
          }

     }

     public boolean esCorrecta(String username, String password) {

          List<Usuario> usuarios = repositorioUsuarios.filtrarPorNombre(username);

          String passwordEncriptada = new Encriptador().encriptarContrasenia(password);

          return usuarios.get(0).getContrasenia().getContrasenia().equals(passwordEncriptada);
     }
}
