package models.repositories.datos;


import models.entities.domain.incidentes.Incidente;
import models.entities.domain.notificaciones.tiempoDeEnvio.ModoRecepcion;
import models.entities.domain.registro.Usuario;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import lombok.Getter;


import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

@Getter

public class RepositorioUsuarios implements WithSimplePersistenceUnit, ICrudRepository {

    private static RepositorioUsuarios instancia = null;

    //Esto se usa unicamente para no romper los tests
    private static List<Usuario> usuariosRegistrados = new ArrayList<Usuario>();


    public static RepositorioUsuarios getInstance(){
        if(instancia==null) {
            instancia = new RepositorioUsuarios();
        }

        return instancia;
    }

    @Override
    public List buscarTodos() {
        return entityManager().createQuery("from " + Usuario.class.getName()).getResultList();
    }

    @Override
    public Object buscar(Long id) {
        return entityManager().find(Usuario.class, id);
    }

    @Override
    public void guardar(Object o) {

        EntityTransaction tx = entityManager().getTransaction();
        tx.begin();
        entityManager().persist(o);
        tx.commit();
    }

    @Override
    public void eliminar(Object o) {
        entityManager().remove(o);
    }

    @Override
    public void actualizar(Object o) {
        withTransaction(() -> { entityManager().merge(o);});
    }


    public static List<Usuario> getUsuariosRegistrados() { return usuariosRegistrados; }
    public void agregarUsuario(Usuario usuario ) {
        withTransaction( () -> {
            if(noEstaRegistrado(usuario.getNombreDeUsuario())) {
                entityManager().persist(usuario);
            }


        });
    }

    public boolean noEstaRegistrado(String nombreUsuario) {

        List usuarios = this.filtrarPorNombre(nombreUsuario);

        return usuarios.isEmpty();
    }

    public List usuariosConNotificacionesAsincronicas() {

        return entityManager().createQuery("from Usuario where modoRecepcion = :asincronica").
                setParameter("asincronica", ModoRecepcion.ASINCRONICA).getResultList();

    }

    public List filtrarPorNombre(String nombreUsuario) {
        List usuarios = entityManager().createQuery("from Usuario where nombreDeUsuario = :nombre")
                .setParameter("nombre", nombreUsuario).getResultList();

        return usuarios;
    }

    public List getUsuariosPersistentes() {
        return entityManager().createQuery("from Usuario").getResultList();
    }

}