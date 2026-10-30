package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.boundary;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control.PagoDetalleDAOInterface;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.PagoDetalle;

@Named("pagoDetalleFrm")
@ViewScoped
public class PagoDetalleFrm implements Serializable {

    @Inject
    private PagoDetalleDAOInterface pagoDetalleDAO;

    private PagoDetalle registroSeleccionado;
    private List<PagoDetalle> registros;

    @PostConstruct
    public void init() {
        this.registroSeleccionado = new PagoDetalle();
        this.registros = pagoDetalleDAO.obtenerTodos();
    }

    public void btnGuardarHandler() {
        try {
            if (this.registroSeleccionado.getIdPagoDetalle() == null) {
                this.registroSeleccionado.setIdPagoDetalle(UUID.randomUUID());
                pagoDetalleDAO.crear(this.registroSeleccionado);
            } else {
                pagoDetalleDAO.modificar(this.registroSeleccionado);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Detalle de pago guardado."));
            nuevoRegistro();
            cargarRegistros();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar el detalle: " + e.getMessage()));
        }
    }

    public void seleccionar(PagoDetalle detalle) { this.registroSeleccionado = detalle; }
    public void nuevoRegistro() { this.registroSeleccionado = new PagoDetalle(); }
    public void eliminar(PagoDetalle detalle) {
        try {
            pagoDetalleDAO.eliminar(detalle);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Detalle eliminado."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el detalle: " + e.getMessage()));
        }
        nuevoRegistro();
        cargarRegistros();
    }
    private void cargarRegistros() { this.registros = pagoDetalleDAO.obtenerTodos(); }

    public PagoDetalle getRegistroSeleccionado() { return registroSeleccionado; }
    public void setRegistroSeleccionado(PagoDetalle registroSeleccionado) { this.registroSeleccionado = registroSeleccionado; }
    public List<PagoDetalle> getRegistros() { return registros; }
}
