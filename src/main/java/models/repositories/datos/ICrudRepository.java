package models.repositories.datos;
import java.util.List;

public interface ICrudRepository {

    public List buscarTodos();
    public Object buscar(Long id);
    public void guardar(Object o);
    public void actualizar(Object o);
    public void eliminar(Object o);
}
