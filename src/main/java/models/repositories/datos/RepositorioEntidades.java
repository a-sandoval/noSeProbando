package models.repositories.datos;

import models.entities.domain.entidades.Entidad;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;


import javax.persistence.EntityTransaction;
import java.util.List;

public class RepositorioEntidades implements WithSimplePersistenceUnit, ICrudRepository {

    @Override
    public List buscarTodos() {
        return entityManager().createQuery("from " + Entidad.class.getName()).getResultList();
    }

    @Override
    public Object buscar(Long id) {
        return entityManager().find(Entidad.class, id);
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


    public List<String> buscarTodosLosNombresDeEntidades() {
        String jpql = "SELECT e.nombre FROM Entidad e";
        return entityManager().createQuery(jpql, String.class).getResultList();
    }

    public List<String> buscarTodosLosNombresDeEntidadesDeInteres(Long idUsuario) {
        String jpql = "SELECT DISTINCT e.nombre FROM Usuario u " +
                "JOIN u.entidadesDeInteres e " +
                "WHERE u.id = :idUsuario";

        return entityManager()
                .createQuery(jpql, String.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    public List filtrarPorNombre(String nombreEntidad) {
        List entidades = entityManager().createQuery("from Entidad where nombre = :nombre")
                .setParameter("nombre", nombreEntidad).getResultList();

        return entidades;
    }
}


