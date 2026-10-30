package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.beans;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control.PagoDAOInterface;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.Pago;

@Named("pagoBean")
@ViewScoped
public class PagoBean implements Serializable {

    @Inject
    private PagoDAOInterface dao;

    private Pago registro = new Pago();

    public List<Pago> getLista() {
        return dao.findRange(0, 100);
    }

    public void guardar() {
        try {
            if (registro.getIdPago() == null) { registro.setIdPago(UUID.randomUUID()); dao.create(registro); }
            else { dao.edit(registro); }
            mensaje(FacesMessage.SEVERITY_INFO, "Éxito", "Pago guardado correctamente.");
            nuevoRegistro();
        } catch (Exception e) {
            mensaje(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar el pago: " + e.getMessage());
        }
    }
    public void seleccionar(Pago pago) { registro = pago; }
    public void eliminar(Pago pago) {
        try { dao.remove(pago); mensaje(FacesMessage.SEVERITY_INFO, "Éxito", "Pago eliminado."); }
        catch (Exception e) { mensaje(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el pago: " + e.getMessage()); }
        nuevoRegistro();
    }
    public void nuevoRegistro() { registro = new Pago(); }
    private void mensaje(FacesMessage.Severity nivel, String titulo, String texto) { FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(nivel, titulo, texto)); }

    public Pago getRegistro() {
        return registro;
    }

    public void setRegistro(Pago registro) {
        this.registro = registro;
    }
}
