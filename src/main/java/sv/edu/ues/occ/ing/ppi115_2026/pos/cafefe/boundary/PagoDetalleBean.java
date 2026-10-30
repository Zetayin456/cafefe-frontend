package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.boundary;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control.PagoDetalleDAOInterface;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.PagoDetalle;

@Named("pagoDetalleBean")
@ViewScoped
public class PagoDetalleBean implements Serializable {

    @Inject
    private PagoDetalleDAOInterface pagoDetalleDAO;

    private PagoDetalle registro;
    private List<PagoDetalle> lista;

    @PostConstruct
    public void init() {
        this.registro = new PagoDetalle();
        this.lista = pagoDetalleDAO.obtenerTodos();
    }

    public void guardar() {
        if (this.registro.getIdPagoDetalle() == null) {
            this.registro.setIdPagoDetalle(UUID.randomUUID());
            pagoDetalleDAO.crear(this.registro);
        } else {
            pagoDetalleDAO.modificar(this.registro);
        }
        this.init();
    }

    public PagoDetalle getRegistro() { return registro; }
    public void setRegistro(PagoDetalle registro) { this.registro = registro; }
    public List<PagoDetalle> getLista() { return lista; }
}
