package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control;

import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.TipoProducto;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface TipoProductoDAOInterface
        extends DaoInterface<TipoProducto> {

    List<TipoProducto> findActivos();
}
