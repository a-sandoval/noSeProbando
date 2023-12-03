package models.repositories.datos;

import models.entities.domain.entidades.OrganismoDeControl;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityTransaction;
import java.util.List;

public class RepositorioOrganismosDeControl implements WithSimplePersistenceUnit, ICrudRepository {

    private static RepositorioOrganismosDeControl instancia = null;

    public static RepositorioOrganismosDeControl getInstance(){
        if (instancia == null) {instancia = new RepositorioOrganismosDeControl();}
        return instancia;
    }

    @Override
    public List buscarTodos() {
        return entityManager().createQuery("from " + OrganismoDeControl.class.getName()).getResultList();
    }

    @Override
    public Object buscar(Long id) {
        return entityManager().find(OrganismoDeControl.class, id);
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

    public boolean estaRegistrado(String cuit) {

        List<OrganismoDeControl> organismosDeControl = entityManager().createQuery("from OrganismoDeControl where CUIT = :c")
                .setParameter("c", cuit).getResultList();

        return !organismosDeControl.isEmpty();
    }



}
