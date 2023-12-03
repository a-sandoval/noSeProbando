package models.repositories.datos;

import models.entities.domain.entidades.PrestadoraDeServicio;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityTransaction;
import java.util.List;

public class RepositorioPrestadorasDeServicio implements WithSimplePersistenceUnit,ICrudRepository {

    private static RepositorioPrestadorasDeServicio instancia = null;

    public static RepositorioPrestadorasDeServicio getInstance(){
        if (instancia == null) {instancia = new RepositorioPrestadorasDeServicio(); }
        return instancia;
    }
    @Override
    public List buscarTodos() {
        return entityManager().createQuery("from " + PrestadoraDeServicio.class.getName()).getResultList();
    }

    @Override
    public Object buscar(Long id) {
        return entityManager().find(PrestadoraDeServicio.class, id);
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
