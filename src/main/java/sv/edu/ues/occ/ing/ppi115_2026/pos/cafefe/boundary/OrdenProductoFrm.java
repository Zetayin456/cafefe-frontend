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
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control.OrdenProductoDAOInterface;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.OrdenProducto;

@Named("ordenProductoFrm")
@ViewScoped
public class OrdenProductoFrm implements Serializable {

    @Inject
    private OrdenProductoDAOInterface ordenProductoDAO;

    private OrdenProducto registroSeleccionado;
    private LazyDataModel<OrdenProducto> modelo;

    private boolean editando;


    @PostConstruct
    public void init() {
        nuevoRegistro();
        this.modelo = new LazyDataModel<OrdenProducto>() {
            @Override
            public String getRowKey(OrdenProducto ordenProducto) {
                if (ordenProducto != null && ordenProducto.getIdOrdenProducto() != null) {
                    return ordenProducto.getIdOrdenProducto().toString();
                }
                return null;
            }

            @Override
            public OrdenProducto getRowData(String rowKey) {
                if (rowKey != null && !rowKey.trim().isEmpty()) {
                    try {
                        return ordenProductoDAO.buscarPorId(rowKey);
                    } catch (Exception e) {
                        return null;
                    }
                }
                return null;
            }

            @Override
            public List<OrdenProducto> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                List<OrdenProducto> lista = ordenProductoDAO.findRange(first, pageSize);
                setRowCount(ordenProductoDAO.count());
                return lista;
            }

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return ordenProductoDAO.count();
            }
        };
    }

 public void nuevoRegistro() {
    this.registroSeleccionado = new OrdenProducto();
    this.registroSeleccionado.setIdOrdenProducto(UUID.randomUUID());
    this.editando = false;
}

 public void seleccionarRegistro(OrdenProducto ordenProducto) {
    if (ordenProducto != null) {
        this.registroSeleccionado = ordenProducto;
        this.editando = true;
    }
}


 public void btnGuardarHandler() {
    try {
        if (registroSeleccionado.getPrecio() == null
                || registroSeleccionado.getPrecio().signum() < 0) {
            mostrarMensaje(
                    FacesMessage.SEVERITY_WARN,
                    "Validación",
                    "El precio debe ser mayor o igual a cero."
            );
            return;
        }

        if (editando) {
            ordenProductoDAO.modificar(registroSeleccionado);
            mostrarMensaje(
                    FacesMessage.SEVERITY_INFO,
                    "Éxito",
                    "Orden-Producto modificada correctamente"
            );
        } else {
            if (registroSeleccionado.getIdOrdenProducto() == null) {
                registroSeleccionado.setIdOrdenProducto(UUID.randomUUID());
            }

            ordenProductoDAO.crear(registroSeleccionado);
            mostrarMensaje(
                    FacesMessage.SEVERITY_INFO,
                    "Éxito",
                    "Orden-Producto registrada correctamente"
            );
        }

        nuevoRegistro();

    } catch (Exception e) {
        mostrarMensaje(
                FacesMessage.SEVERITY_ERROR,
                "Error",
                "No se pudo guardar la información."
        );
    }
}

public boolean isEditando() {
    return editando;
}

public void setEditando(boolean editando) {
    this.editando = editando;
}





    public void btnEliminarHandler() {
        try {
            ordenProductoDAO.eliminar(registroSeleccionado);
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
    public OrdenProducto getRegistroSeleccionado() { return registroSeleccionado; }
    public void setRegistroSeleccionado(OrdenProducto registroSeleccionado) { this.registroSeleccionado = registroSeleccionado; }
    public LazyDataModel<OrdenProducto> getModelo() { return modelo; }
}
