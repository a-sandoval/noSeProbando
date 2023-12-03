package models.repositories.datos;

import models.entities.domain.comunidad.Comunidad;
import models.entities.domain.incidentes.Incidente;
import models.entities.domain.registro.Usuario;
import models.entities.domain.services.georef.entities.Ubicacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import lombok.Getter;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RepositorioIncidentes implements WithSimplePersistenceUnit, ICrudRepository {

    private static RepositorioIncidentes instancia = null;

    private List<Incidente> incidentesForTest = new ArrayList<>();

    public static RepositorioIncidentes getInstance(){
        if (instancia == null){
            instancia = new RepositorioIncidentes();

        }
        return instancia;
    }

    @Override
    public List buscarTodos() {
        return entityManager().createQuery("from " + Incidente.class.getName()).getResultList();
    }

    @Override
    public Object buscar(Long id) {
        return entityManager().find(Incidente.class, id);
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

    public List<Incidente> filtrarUltimaSemana() {
            EntityManager entityManager; // Asegúrate de inicializar este EntityManager en algún lugar

            String jpql = "SELECT i FROM Incidente i WHERE i.fechaReporte > :fecha";
            Query query = entityManager().createQuery(jpql);
            LocalDateTime fecha = LocalDateTime.now().minusWeeks(1);
            query.setParameter("fecha", fecha);

            List<Incidente> incidentes = query.getResultList();
            return incidentes;
        }


    public List filtrarPorUbicacionCercana(Ubicacion ubicacion) {

        List incidentesCerca = null; // Inicializamos la lista

        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<Incidente> criteriaQuery = cb.createQuery(Incidente.class);
        Root<Incidente> root = criteriaQuery.from(Incidente.class);

        criteriaQuery.select(root).where(root.get("fechaResolucion").isNull());

        List<Incidente> incidentesSinResolver = entityManager().createQuery(criteriaQuery).getResultList();

        incidentesCerca = incidentesSinResolver.stream().filter(incidente -> incidente.getUbicacion().estasCercaDe(ubicacion)).
                    collect(Collectors.toList());


        return incidentesCerca; // Devolvemos la lista de incidentes

    }


    public List<Incidente> getIncidentes() {

       return entityManager().createQuery("from Incidente").getResultList();
    }

    public List<Incidente> filtrarPorComunidad(Comunidad comunidad) {

        List usuarios = entityManager().createQuery("from Incidente where comunidadDondeSeReporta = :comunidad")
                .setParameter("comunidad", comunidad).getResultList();

        return usuarios;
    }


    public List<Incidente> buscarIncidentesDeInteresPara(Long idUsuario) {

        Usuario usuario = (Usuario) new RepositorioUsuarios().buscar(idUsuario);

        List incidentesDeInteres = null;

        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<Incidente> criteriaQuery = cb.createQuery(Incidente.class);
        Root<Incidente> root = criteriaQuery.from(Incidente.class);

        criteriaQuery.select(root).where(root.get("fechaResolucion").isNull());

        List<Incidente> incidentesSinResolver = entityManager().createQuery(criteriaQuery).getResultList();

        incidentesDeInteres = incidentesSinResolver.stream().filter(incidente -> usuario.teInteresa(incidente)).
                collect(Collectors.toList());


        return incidentesDeInteres;


    }


}
