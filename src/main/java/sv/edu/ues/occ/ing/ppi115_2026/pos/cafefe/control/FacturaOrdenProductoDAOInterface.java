package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control;

import jakarta.ejb.Local;
import java.util.List;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.FacturaOrdenProducto;

@Local
public interface FacturaOrdenProductoDAOInterface extends DaoInterface<FacturaOrdenProducto> {
    void crear(FacturaOrdenProducto registro);
    FacturaOrdenProducto modificar(FacturaOrdenProducto registro);
    void eliminar(FacturaOrdenProducto registro);
    FacturaOrdenProducto buscarPorId(Object id);
    List<FacturaOrdenProducto> findRange(int first, int pageSize);
    List<FacturaOrdenProducto> findAllWithRelations();
}
