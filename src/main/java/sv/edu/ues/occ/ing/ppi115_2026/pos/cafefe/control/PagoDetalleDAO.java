package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.PagoDetalle;

@Stateless
public class PagoDetalleDAO extends DefaultDAO<PagoDetalle> implements PagoDetalleDAOInterface {

    @PersistenceContext(unitName = "cafefePU")
    private EntityManager em;

    public PagoDetalleDAO() { super(PagoDetalle.class); }

    @Override
    protected EntityManager getEntityManager() { return em; }

    @Override
    public void crear(PagoDetalle entity) {
        create(entity);
    }

    @Override
    public PagoDetalle modificar(PagoDetalle entity) {
        return edit(entity);
    }

    @Override
    public void eliminar(PagoDetalle entity) {
        remove(entity);
    }

    @Override
    public List<PagoDetalle> obtenerTodos() {
        return em.createQuery("SELECT pd FROM PagoDetalle pd LEFT JOIN FETCH pd.idPago ORDER BY pd.idPagoDetalle", PagoDetalle.class).getResultList();
    }
}
