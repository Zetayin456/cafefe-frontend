package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control;
import jakarta.ejb.Local;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.ProductoTipoProducto;

@Local
public interface ProductoTipoProductoDAOInterface extends DaoInterface<ProductoTipoProducto> {
    List<ProductoTipoProducto> findAllWithRelations();
    List<ProductoTipoProducto> findByProducto(UUID idProducto);
}
