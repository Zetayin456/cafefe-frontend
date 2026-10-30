package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control;

import java.util.List;
import jakarta.ejb.Local;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.Orden;

@Local
public interface OrdenDAOInterface extends DaoInterface<Orden> {
    void crear(Orden orden);
    Orden modificar(Orden orden);
    void eliminar(Orden orden);
    List<Orden> obtenerTodos();
    List<Orden> findAllWithDetails();
}
