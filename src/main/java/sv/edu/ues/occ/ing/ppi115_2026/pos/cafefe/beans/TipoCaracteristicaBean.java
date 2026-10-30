package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.beans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control.TipoCaracteristicaDAOInterface;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.TipoCaracteristica;

@Named("tipoCaracteristicaBean")
@ViewScoped
public class TipoCaracteristicaBean implements Serializable {

    @Inject
    private TipoCaracteristicaDAOInterface dao;

    private TipoCaracteristica registro;
    private List<TipoCaracteristica> lista;

    @PostConstruct
    public void init() {
        limpiar();
        cargarLista();
    }

    public void cargarLista() {
        if (dao != null) {
            this.lista = dao.findRange(0, 100);
        }
    }

    public void limpiar() {
        this.registro = new TipoCaracteristica();
        this.registro.setActivo(true);
    }

    public void seleccionar(TipoCaracteristica tipo) { this.registro = tipo; }
    public void eliminar(TipoCaracteristica tipo) {
        try {
            dao.remove(tipo);
            mostrarMensaje("Éxito", "Tipo de característica eliminado correctamente");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el tipo: " + e.getMessage()));
        }
        limpiar();
        cargarLista();
    }

    public void guardar() {
        try {
            if (registro != null && dao != null) {
                String expresion = registro.getExpresionRegular();
                if (expresion != null && !expresion.isBlank()) {
                    try {
                        java.util.regex.Pattern.compile(expresion);
                    } catch (java.util.regex.PatternSyntaxException ex) {
                        mostrarMensaje("Validación", "La expresión regular no es válida: " + ex.getDescription());
                        return;
                    }
                }
                if (registro.getIdTipoCaracteristica() == null) {
                    registro.setIdTipoCaracteristica(UUID.randomUUID());
                    dao.create(registro);
                    mostrarMensaje("Éxito", "Tipo de característica creado correctamente");
                } else {
                    dao.edit(registro);
                    mostrarMensaje("Éxito", "Tipo de característica actualizado correctamente");
                }
                limpiar();
                cargarLista();
            }
        } catch (Exception e) {
            mostrarMensaje("Error", "No se pudo guardar el registro: " + e.getMessage());
        }
    }

    private void mostrarMensaje(String resumen, String detalle) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, resumen, detalle));
    }

    public TipoCaracteristica getRegistro() {
        return registro;
    }

    public void setRegistro(TipoCaracteristica registro) {
        this.registro = registro;
    }

    public List<TipoCaracteristica> getLista() {
        return lista;
    }
}
