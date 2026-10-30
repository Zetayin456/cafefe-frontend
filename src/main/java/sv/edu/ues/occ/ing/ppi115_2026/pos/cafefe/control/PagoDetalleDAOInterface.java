package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control;

import jakarta.ejb.Local;
import java.util.List;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.PagoDetalle;

@Local
public interface PagoDetalleDAOInterface extends DaoInterface<PagoDetalle> {
    void crear(PagoDetalle registro);
    PagoDetalle modificar(PagoDetalle registro);
    void eliminar(PagoDetalle registro);
    List<PagoDetalle> obtenerTodos();
}
