package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.Producto;

@Stateless
public class ProductoDAO extends DefaultDAO<Producto> implements ProductoDAOInterface {

    @PersistenceContext(unitName = "cafefePU")
    private EntityManager em;

    public ProductoDAO() {
        super(Producto.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<Producto> findActivos() {
        return em.createQuery("SELECT p FROM Producto p WHERE p.activo = TRUE ORDER BY p.nombre", Producto.class)
                .getResultList();
    }

    @Override
    public List<Producto> findActivosConDescuentos() {
        return em.createQuery("SELECT DISTINCT p FROM Producto p "
                + "LEFT JOIN FETCH p.descuentoProductoList dp "
                + "LEFT JOIN FETCH dp.idDescuento d "
                + "LEFT JOIN FETCH d.idTipoDescuento "
                + "WHERE p.activo = TRUE ORDER BY p.nombre", Producto.class).getResultList();
    }
}
