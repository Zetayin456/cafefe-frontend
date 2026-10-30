package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control;

import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.TipoProducto;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class TipoProductoDAO extends DefaultDAO<TipoProducto>
        implements TipoProductoDAOInterface, Serializable {

    @PersistenceContext(unitName = "cafefePU")
    private EntityManager em;

    public TipoProductoDAO() {
        super(TipoProducto.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<TipoProducto> findActivos() {
        return em.createQuery("SELECT t FROM TipoProducto t WHERE t.activo = TRUE ORDER BY t.nombre", TipoProducto.class)
                .getResultList();
    }
}
