package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.FacturaOrdenProducto;

@Stateless
public class FacturaOrdenProductoDAO extends DefaultDAO<FacturaOrdenProducto>
        implements FacturaOrdenProductoDAOInterface, Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "cafefePU")
    private EntityManager em;

    public FacturaOrdenProductoDAO() { super(FacturaOrdenProducto.class); }

    @Override
    protected EntityManager getEntityManager() { return em; }

    @Override
    public void crear(FacturaOrdenProducto registro) { create(registro); }

    @Override
    public FacturaOrdenProducto modificar(FacturaOrdenProducto registro) { return edit(registro); }

    @Override
    public void eliminar(FacturaOrdenProducto registro) {
        if (registro == null || registro.getIdFacturaOrdenProducto() == null) {
            throw new IllegalArgumentException("El registro a eliminar no es válido.");
        }
        FacturaOrdenProducto managed = find(registro.getIdFacturaOrdenProducto());
        if (managed != null) em.remove(managed);
    }

    @Override
    public FacturaOrdenProducto buscarPorId(Object id) {
        if (id == null) return null;
        try {
            UUID uuid = id instanceof UUID value ? value : UUID.fromString(id.toString());
            return find(uuid);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public List<FacturaOrdenProducto> findAllWithRelations() {
        return em.createQuery("SELECT DISTINCT fop FROM FacturaOrdenProducto fop "
                + "LEFT JOIN FETCH fop.idFactura "
                + "LEFT JOIN FETCH fop.idOrdenProducto op "
                + "LEFT JOIN FETCH op.idProducto ORDER BY fop.idFacturaOrdenProducto",
                FacturaOrdenProducto.class).getResultList();
    }

    @Override
    public List<FacturaOrdenProducto> findRange(int first, int pageSize) {
        if (first < 0 || pageSize <= 0) throw new IllegalArgumentException("Parámetros de paginación inválidos.");
        return em.createQuery("SELECT fop FROM FacturaOrdenProducto fop ORDER BY fop.idFacturaOrdenProducto", FacturaOrdenProducto.class)
                .setFirstResult(first).setMaxResults(pageSize).getResultList();
    }
}
