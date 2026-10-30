package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.boundary;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control.FacturaOrdenProductoDAOInterface;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.FacturaOrdenProducto;

@Named("facturaOrdenProductoFrm")
@ViewScoped
public class FacturaOrdenProductoFrm implements Serializable {

    @Inject
    private FacturaOrdenProductoDAOInterface facturaOrdenProductoDAO;

    private FacturaOrdenProducto registroSeleccionado;
    private LazyDataModel<FacturaOrdenProducto> modelo;

    @PostConstruct
    public void init() {
        nuevoRegistro();
        this.modelo = new LazyDataModel<FacturaOrdenProducto>() {
            @Override
            public String getRowKey(FacturaOrdenProducto fop) {
                if (fop != null && fop.getIdFacturaOrdenProducto() != null) {
                    return fop.getIdFacturaOrdenProducto().toString();
                }
                return null;
            }

            @Override
            public FacturaOrdenProducto getRowData(String rowKey) {
                if (rowKey != null && !rowKey.trim().isEmpty()) {
                    try {
                        return facturaOrdenProductoDAO.buscarPorId(rowKey);
                    } catch (Exception e) {
                        return null;
                    }
                }
                return null;
            }

            @Override
            public List<FacturaOrdenProducto> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                List<FacturaOrdenProducto> lista = facturaOrdenProductoDAO.findRange(first, pageSize);
                setRowCount(facturaOrdenProductoDAO.count());
                return lista;
            }

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return facturaOrdenProductoDAO.count();
            }
        };
    }

    public void nuevoRegistro() {
        this.registroSeleccionado = new FacturaOrdenProducto();
        this.registroSeleccionado.setIdFacturaOrdenProducto(UUID.randomUUID());
    }

    public void btnGuardarHandler() {
        try {
            if (registroSeleccionado.getIdFacturaOrdenProducto() == null) {
                registroSeleccionado.setIdFacturaOrdenProducto(UUID.randomUUID());
            }

            boolean existe = false;
            if (registroSeleccionado.getIdFacturaOrdenProducto() != null) {
                FacturaOrdenProducto fop = facturaOrdenProductoDAO.buscarPorId(registroSeleccionado.getIdFacturaOrdenProducto().toString());
                if (fop != null) {
                    existe = true;
                }
            }

            if (!existe) {
                facturaOrdenProductoDAO.crear(registroSeleccionado);
                mostrarMensaje(FacesMessage.SEVERITY_INFO, "Éxito", "Registro creado correctamente");
            } else {
                facturaOrdenProductoDAO.modificar(registroSeleccionado);
                mostrarMensaje(FacesMessage.SEVERITY_INFO, "Éxito", "Registro modificado correctamente");
            }
            nuevoRegistro();
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar la información");
        }
    }

    public void btnEliminarHandler() {
        try {
            facturaOrdenProductoDAO.eliminar(registroSeleccionado);
            mostrarMensaje(FacesMessage.SEVERITY_INFO, "Éxito", "Registro eliminado correctamente");
            nuevoRegistro();
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el registro");
        }
    }

    private void mostrarMensaje(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    // Getters y Setters
    public FacturaOrdenProducto getRegistroSeleccionado() { return registroSeleccionado; }
    public void setRegistroSeleccionado(FacturaOrdenProducto registroSeleccionado) { this.registroSeleccionado = registroSeleccionado; }
    public LazyDataModel<FacturaOrdenProducto> getModelo() { return modelo; }
}
