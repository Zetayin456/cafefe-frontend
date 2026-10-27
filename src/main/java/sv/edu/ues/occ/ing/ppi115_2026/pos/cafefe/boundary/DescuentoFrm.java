
package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.boundary;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control.DescuentoDAOInterface;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control.TipoDescuentoDAOInterface;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.Descuento;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.TipoDescuento;

@Named(value = "descuentoFrm")
@ViewScoped
public class DescuentoFrm implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER =
            Logger.getLogger(DescuentoFrm.class.getName());

    @Inject
    private DescuentoDAOInterface descuentoDAO;

    @Inject
    private TipoDescuentoDAOInterface tipoDescuentoDAO;

    private Descuento registro;
    private List<Descuento> listaRegistros;
    private List<TipoDescuento> listaTiposDescuento;
    private UUID idTipoDescuentoSeleccionado;

    @PostConstruct
    public void init() {
        limpiar();
        cargarDatos();
    }

    public void cargarDatos() {
        try {
            List<Descuento> descuentos = descuentoDAO.findRange(0, 1000);
            listaRegistros = descuentos != null
                    ? descuentos : new ArrayList<>();

            List<TipoDescuento> tipos = tipoDescuentoDAO.findRange(0, 1000);
            listaTiposDescuento = tipos != null
                    ? tipos.stream()
                            .filter(t -> Boolean.TRUE.equals(t.getActivo()))
                            .toList()
                    : new ArrayList<>();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al cargar descuentos", ex);
            mensaje(FacesMessage.SEVERITY_ERROR,
                    "Error", "No se pudieron cargar los datos.");
        }
    }

    public void limpiar() {
        registro = new Descuento();
        idTipoDescuentoSeleccionado = null;
    }

    public void seleccionarRegistro(Descuento d) {
        if (d == null) {
            return;
        }

        // Copiamos los datos para editar sin modificar
        // directamente el objeto de la tabla.
        registro = new Descuento();
        registro.setIdDescuento(d.getIdDescuento());
        registro.setNombre(d.getNombre());
        registro.setFechaDesde(d.getFechaDesde());
        registro.setFechaHasta(d.getFechaHasta());
        registro.setObservaciones(d.getObservaciones());
        registro.setIdTipoDescuento(d.getIdTipoDescuento());

        idTipoDescuentoSeleccionado = d.getIdTipoDescuento() != null
                ? d.getIdTipoDescuento().getIdTipoDescuento()
                : null;
    }

    public void btnGuardarHandler() {
        try {
            if (registro == null) {
                mensaje(FacesMessage.SEVERITY_WARN,
                        "Aviso", "No hay un descuento seleccionado.");
                return;
            }

            if (registro.getNombre() == null
                    || registro.getNombre().isBlank()) {
                mensaje(FacesMessage.SEVERITY_WARN,
                        "Validación", "Escribe el nombre del descuento.");
                return;
            }

            if (idTipoDescuentoSeleccionado == null) {
                mensaje(FacesMessage.SEVERITY_WARN,
                        "Validación", "Selecciona un tipo de descuento.");
                return;
            }

            Date desde = registro.getFechaDesde();
            Date hasta = registro.getFechaHasta();

            if (desde != null && hasta != null && hasta.before(desde)) {
                mensaje(FacesMessage.SEVERITY_WARN,
                        "Validación",
                        "La fecha final no puede ser anterior a la inicial.");
                return;
            }

            TipoDescuento tipo = tipoDescuentoDAO.find(
                    idTipoDescuentoSeleccionado);

            if (tipo == null || !Boolean.TRUE.equals(tipo.getActivo())) {
                mensaje(FacesMessage.SEVERITY_ERROR,
                        "Error",
                        "El tipo de descuento no existe o está inactivo.");
                cargarDatos();
                return;
            }

            registro.setNombre(registro.getNombre().trim());
            registro.setIdTipoDescuento(tipo);

            if (registro.getIdDescuento() == null) {
                registro.setIdDescuento(UUID.randomUUID());
                descuentoDAO.create(registro);
                mensaje(FacesMessage.SEVERITY_INFO,
                        "Éxito", "Descuento creado correctamente.");
            } else {
                descuentoDAO.edit(registro);
                mensaje(FacesMessage.SEVERITY_INFO,
                        "Éxito", "Descuento actualizado correctamente.");
            }

            limpiar();
            cargarDatos();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al guardar descuento", ex);
            mensaje(FacesMessage.SEVERITY_ERROR,
                    "Error", "No se pudo guardar el descuento.");
        }
    }

    public void eliminarRegistro(Descuento d) {
        if (d == null || d.getIdDescuento() == null) {
            mensaje(FacesMessage.SEVERITY_WARN,
                    "Aviso", "Selecciona un descuento válido.");
            return;
        }

        try {
            // Eliminación física: puede fallar si el descuento
            // está relacionado con registros de DescuentoProducto.
            descuentoDAO.remove(d);

            if (registro != null
                    && d.getIdDescuento().equals(registro.getIdDescuento())) {
                limpiar();
            }

            cargarDatos();
            mensaje(FacesMessage.SEVERITY_INFO,
                    "Éxito", "Descuento eliminado correctamente.");

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al eliminar descuento", ex);
            mensaje(FacesMessage.SEVERITY_ERROR,
                    "No se pudo eliminar",
                    "Puede que este descuento esté asociado a productos.");
        }
    }

    private void mensaje(FacesMessage.Severity severidad,
                         String titulo, String detalle) {
        FacesContext.getCurrentInstance().addMessage(
                null, new FacesMessage(severidad, titulo, detalle));
    }

    public Descuento getRegistro() {
        return registro;
    }

    public void setRegistro(Descuento registro) {
        this.registro = registro;
    }

    public List<Descuento> getListaRegistros() {
        return listaRegistros;
    }

    public void setListaRegistros(List<Descuento> listaRegistros) {
        this.listaRegistros = listaRegistros;
    }

    public List<TipoDescuento> getListaTiposDescuento() {
        return listaTiposDescuento;
    }

    public void setListaTiposDescuento(List<TipoDescuento> listaTiposDescuento) {
        this.listaTiposDescuento = listaTiposDescuento;
    }

    public UUID getIdTipoDescuentoSeleccionado() {
        return idTipoDescuentoSeleccionado;
    }

    public void setIdTipoDescuentoSeleccionado(UUID idTipoDescuentoSeleccionado) {
        this.idTipoDescuentoSeleccionado = idTipoDescuentoSeleccionado;
    }
}
