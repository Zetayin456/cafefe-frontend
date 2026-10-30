
package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control;

import jakarta.ejb.Local;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.EmpleadoRol;

@Local
public interface EmpleadoRolDAOInterface extends DaoInterface<EmpleadoRol> {
    List<EmpleadoRol> findByEmpleado(UUID idEmpleado);
    List<EmpleadoRol> findAllWithRelations();
}
