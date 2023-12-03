package models.repositories.datos;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.entities.domain.roles.Permiso;
import javax.persistence.EntityTransaction;
import java.util.List;

public class RepositorioDePermisos implements WithSimplePersistenceUnit, ICrudRepository {

    public Permiso buscarPermisoPorNombre(String nombre) {
        return (Permiso) entityManager()
                .createQuery("from Permiso where nombreInterno = :nombre")
                .setParameter("nombre", nombre)
                .getSingleResult();
    }

    @Override
    public List buscarTodos() {
        return entityManager().createQuery("from " + Permiso.class.getName()).getResultList();
    }

    @Override
    public Object buscar(Long id) {
        return entityManager().find(Permiso.class, id);
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

}
