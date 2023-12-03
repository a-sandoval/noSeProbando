package models.repositories.datos;

import models.entities.domain.comunidad.Comunidad;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class RepositorioComunidades implements WithSimplePersistenceUnit, ICrudRepository{

    public Comunidad obtenerComunidad(Long id) { //TODO
        return find(Comunidad.class, id);
    }

    public Long obtenerIdDeComunidadPorNombre(String nombreComunidad) {
        return withTransaction(() -> {
            TypedQuery<Comunidad> query = entityManager().createQuery(
                    "SELECT c FROM Comunidad c WHERE c.nombre = :nombre", Comunidad.class);
            query.setParameter("nombre", nombreComunidad);

            Comunidad comunidad = query.getSingleResult();

            if (comunidad != null) {
                return comunidad.getId();
            } else {
                return null;
            }
        });
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

    @Override
    public List buscarTodos() {
        return entityManager().createQuery("from " + Comunidad.class.getName()).getResultList();
    }

    @Override
    public Object buscar(Long id) {
        return entityManager().find(Comunidad.class, id);
    }

    public List<Long> buscarComunidadesDe(long idUsuario) {

        // Define la consulta JPQL con un parámetro
        String sql = "SELECT comunidad_id FROM comunidad_usuario WHERE miembros_id = :usuarioId";

        // Crea una instancia de Query y asigna el parámetro
        Query query = entityManager().createNativeQuery(sql);
        query.setParameter("usuarioId", idUsuario);

        List<BigInteger> result = query.getResultList();

        // Convierte los valores de BigInteger a Long
        List<Long> comunidadIds = new ArrayList<>();
        for (BigInteger value : result) {
            comunidadIds.add(value.longValue());
        }

        return comunidadIds;

    }

    public List<Comunidad> buscarComunidadesNoDe(Long idUsuario) {
        String sql = "SELECT c.* FROM comunidad c " +
                "WHERE c.id NOT IN " +
                "(SELECT cu.comunidad_id FROM comunidad_usuario cu " +
                "WHERE cu.miembros_id = :usuarioId)";

        Query query = entityManager().createNativeQuery(sql, Comunidad.class);
        query.setParameter("usuarioId", idUsuario);

        List<Comunidad> comunidades = query.getResultList();

        return comunidades;

    }
    public List filtrarPorNombre(String nombreComunidad) {
        List comunidades = entityManager().createQuery("from Comunidad where nombre = :nombre")
                .setParameter("nombre", nombreComunidad).getResultList();

        return comunidades;
    }

    public void eliminarUsuario(Long comunidad, Long id) {
        withTransaction(() -> {
            String sql = "DELETE FROM comunidad_usuario WHERE miembros_id = :Id AND comunidad_id = :Comunidad";

            Query query = entityManager().createNativeQuery(sql);
            query.setParameter("Id", id);
            query.setParameter("Comunidad", comunidad);

            query.executeUpdate();
        });
    }


}
