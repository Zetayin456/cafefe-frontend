package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.beans;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.TestFacesContext;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control.ProductoDAOInterface;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoBeanValidationTest {
    private ProductoBean bean;
    private ProductoDAOInterface dao;
    private FacesContext faces;
    private ArgumentCaptor<FacesMessage> messages;
    @BeforeEach void setUp() throws Exception {
        bean = new ProductoBean(); dao = mock(ProductoDAOInterface.class); faces = mock(FacesContext.class);
        Field f = ProductoBean.class.getDeclaredField("dao"); f.setAccessible(true); f.set(bean, dao);
        TestFacesContext.install(faces); messages = ArgumentCaptor.forClass(FacesMessage.class);
    }
    @Test void rejectsMissingOrInactiveProductType() {
        bean.guardar();
        verify(faces).addMessage(isNull(), messages.capture());
        assertEquals(FacesMessage.SEVERITY_ERROR, messages.getValue().getSeverity());
        TipoProducto inactive = new TipoProducto(); inactive.setActivo(false); bean.setTipoProductoSeleccionado(inactive);
        bean.guardar(); verify(faces, times(2)).addMessage(isNull(), any(FacesMessage.class));
        verifyNoInteractions(dao);
    }
    @Test void rejectsCharacteristicRegexMismatch() {
        TipoProducto tipo = activeType(); bean.setTipoProductoSeleccionado(tipo);
        TipoCaracteristica tc = new TipoCaracteristica(); tc.setExpresionRegular("^[0-9]+$");
        Caracteristica c = new Caracteristica(); c.setIdTipoCaracteristica(tc);
        bean.setCaracteristicaSeleccionada(c); bean.setValorCaracteristica("2x");
        bean.guardar(); verify(faces).addMessage(isNull(), any(FacesMessage.class)); verifyNoInteractions(dao);
    }
    @Test void rejectsDiscountAboveTypeMaximumAndOutsideValidity() {
        bean.setTipoProductoSeleccionado(activeType());
        TipoDescuento td = new TipoDescuento(); td.setDescuentoMaximo(15);
        Descuento d = new Descuento(); d.setIdTipoDescuento(td); bean.setDescuentoSeleccionado(d);
        bean.setValorDescuento(16); bean.guardar(); verify(faces).addMessage(isNull(), any(FacesMessage.class));
        bean.setDescuentoSeleccionado(null); d.setFechaDesde(new Date(System.currentTimeMillis() + 60_000));
        bean.setDescuentoSeleccionado(d); bean.guardar();
        d.setFechaDesde(null); d.setFechaHasta(new Date(System.currentTimeMillis() - 60_000)); bean.guardar();
        verify(faces, times(3)).addMessage(isNull(), any(FacesMessage.class)); verifyNoInteractions(dao);
    }
    @Test void validProductAndDiscountSaveBuildAssociationsThenResetForm() {
        bean.setTipoProductoSeleccionado(activeType());
        TipoCaracteristica tc = new TipoCaracteristica(); tc.setExpresionRegular("[A-Z]+");
        Caracteristica c = new Caracteristica(); c.setIdTipoCaracteristica(tc);
        bean.setCaracteristicaSeleccionada(c); bean.setValorCaracteristica("HOT");
        TipoDescuento td = new TipoDescuento(); td.setDescuentoMaximo(20);
        Descuento d = new Descuento(); d.setIdTipoDescuento(td); bean.setDescuentoSeleccionado(d); bean.setValorDescuento(10);
        bean.guardar();
        ArgumentCaptor<Producto> saved = ArgumentCaptor.forClass(Producto.class); verify(dao).create(saved.capture());
        assertNotNull(saved.getValue().getIdProducto()); assertEquals(1, saved.getValue().getProductoTipoProductoList().size());
        assertEquals("HOT", saved.getValue().getProductoCaracteristicaList().get(0).getValor());
        assertEquals(10, saved.getValue().getDescuentoProductoList().get(0).getValor());
        assertNotNull(bean.getRegistro()); assertNull(bean.getTipoProductoSeleccionado());
        verify(faces).addMessage(isNull(), any(FacesMessage.class));
    }
    private TipoProducto activeType() { TipoProducto t = new TipoProducto(); t.setIdTipoProducto(UUID.randomUUID()); t.setActivo(true); return t; }
}
