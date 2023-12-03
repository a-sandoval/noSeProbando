package models.repositories.datos;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.entities.domain.comunidad.Comunidad;
import models.entities.domain.roles.Rol;

import javax.persistence.EntityTransaction;
import java.util.List;

public class RepositorioDeRoles implements WithSimplePersistenceUnit, ICrudRepository {

    public List buscarRolesPorComunidad(Comunidad comunidad) {

        List roles = entityManager().createQuery("from Rol where comunidad = :comunidad")
                .setParameter("comunidad", comunidad).getResultList();

        return roles;

    }

    @Override
    public List buscarTodos() {
        return entityManager().createQuery("from " + Rol.class.getName()).getResultList();
    }

    @Override
    public Object buscar(Long id) {
        return entityManager().find(Rol.class, id);
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
