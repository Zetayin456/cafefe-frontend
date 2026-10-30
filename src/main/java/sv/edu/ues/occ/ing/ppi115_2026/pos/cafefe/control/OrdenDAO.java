package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.Orden;

@Stateless
public class OrdenDAO extends DefaultDAO<Orden> implements OrdenDAOInterface, Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "cafefePU")
    private EntityManager em;

    public OrdenDAO() { super(Orden.class); }

    @Override
    protected EntityManager getEntityManager() { return em; }

    @Override
    public void crear(Orden entity) {
        create(entity);
    }

    @Override
    public Orden modificar(Orden entity) {
        return edit(entity);
    }

     @Override
     public void eliminar(Orden orden) {
        if (orden == null || orden.getIdOrden() == null) {
            throw new IllegalArgumentException("La orden no es válida.");
        }

        Orden ordenGestionada = find(orden.getIdOrden());

        if (ordenGestionada != null) {
            em.remove(ordenGestionada);
        }
    }


    @Override
    public List<Orden> obtenerTodos() {
        return findAllWithDetails();
    }

    @Override
    public List<Orden> findAllWithDetails() {
        return em.createQuery("SELECT DISTINCT o FROM Orden o "
                + "LEFT JOIN FETCH o.idEmpleadoRol er "
                + "LEFT JOIN FETCH er.idEmpleado "
                + "LEFT JOIN FETCH er.idRol "
                + "LEFT JOIN FETCH o.ordenProductoList op "
                + "LEFT JOIN FETCH op.idProducto "
                + "ORDER BY o.fechaCreacion DESC", Orden.class).getResultList();
    }
}
