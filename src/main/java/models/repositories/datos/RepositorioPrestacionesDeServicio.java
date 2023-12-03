package models.repositories.datos;

import models.entities.domain.incidentes.Incidente;
import models.entities.domain.servicios.PrestacionDeServicio;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class RepositorioPrestacionesDeServicio implements WithSimplePersistenceUnit, ICrudRepository {

    @Override
    public List buscarTodos() {
        return entityManager().createQuery("from " + PrestacionDeServicio.class.getName()).getResultList();
    }

    public List<String> buscarTodosLosNombres() {
        return entityManager()
                .createQuery("SELECT p.nombre FROM PrestacionDeServicio p")
                .getResultList();
    }
    @Override
    public Object buscar(Long id) {
        return entityManager().find(PrestacionDeServicio.class, id);
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
        withTransaction(() -> {
            entityManager().merge(o);
        });
    }


    public Long obtenerIdDelServicioPorNombre(String nombrePrestacion) {
        try {
            TypedQuery<PrestacionDeServicio> query = entityManager().createQuery(
                            "SELECT p FROM PrestacionDeServicio p WHERE p.nombre = :nombre", PrestacionDeServicio.class)
                    .setParameter("nombre", nombrePrestacion);
            PrestacionDeServicio prestacion = query.getSingleResult();
            return prestacion.getId();
        } catch (NoResultException e) {
            // Manejar el caso en el que no se encuentra ninguna coincidencia
            return null;
        }
    }

}
