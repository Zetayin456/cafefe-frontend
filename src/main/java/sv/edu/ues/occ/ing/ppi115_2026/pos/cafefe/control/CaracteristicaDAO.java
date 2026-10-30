package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.Caracteristica;

@Stateless
public class CaracteristicaDAO extends DefaultDAO<Caracteristica> implements CaracteristicaDAOInterface, Serializable {

    @PersistenceContext(unitName = "cafefePU")
    private EntityManager em;

    public CaracteristicaDAO() {
        super(Caracteristica.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<Caracteristica> findAllWithRelations() {
        return em.createQuery("SELECT c FROM Caracteristica c LEFT JOIN FETCH c.idTipoCaracteristica ORDER BY c.nombre", Caracteristica.class)
                .getResultList();
    }
}
