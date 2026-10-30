package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control;

import java.util.List;
import jakarta.ejb.Local;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.Caracteristica;

@Local
public interface CaracteristicaDAOInterface extends DaoInterface<Caracteristica> {
    List<Caracteristica> findAllWithRelations();
}
