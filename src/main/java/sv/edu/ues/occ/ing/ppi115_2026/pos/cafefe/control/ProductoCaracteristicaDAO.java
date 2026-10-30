package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.ProductoCaracteristica;

@Stateless
public class ProductoCaracteristicaDAO extends DefaultDAO<ProductoCaracteristica> implements ProductoCaracteristicaDAOInterface, Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "cafefePU")
    private EntityManager em;

    public ProductoCaracteristicaDAO() {
        super(ProductoCaracteristica.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<ProductoCaracteristica> findAllWithRelations() {
        return em.createQuery("SELECT DISTINCT pc FROM ProductoCaracteristica pc "
                + "JOIN FETCH pc.idProducto JOIN FETCH pc.idCaracteristica c "
                + "LEFT JOIN FETCH c.idTipoCaracteristica ORDER BY pc.idProducto.nombre", ProductoCaracteristica.class)
                .getResultList();
    }
}
