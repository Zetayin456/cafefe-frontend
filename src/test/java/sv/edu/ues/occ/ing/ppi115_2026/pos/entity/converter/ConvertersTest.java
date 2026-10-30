package sv.edu.ues.occ.ing.ppi115_2026.pos.entity.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.component.UIViewRoot;
import jakarta.persistence.AttributeConverter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.lang.reflect.Field;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control.TipoCaracteristicaDAOInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.converters.EntityConverter;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.TipoCaracteristica;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.TestFacesContext;

@ExtendWith(MockitoExtension.class)
class ConvertersTest {
    @Test void uuidAttributeConverterHandlesValidNullAndWrongType() {
        UuidConverter converter = new UuidConverter();
        UUID id = UUID.randomUUID();
        assertEquals(id, converter.convertToEntityAttribute(id.toString()));
        assertNull(converter.convertToEntityAttribute(null));
        assertEquals(id, converter.convertToDatabaseColumn(id));
        assertNull(converter.convertToDatabaseColumn(null));
        assertThrows(IllegalArgumentException.class, () -> converter.convertToEntityAttribute("not a uuid"));
    }

    @Test void uuidFacesConverterHandlesValidAndInvalidStrings() {
        UuidFacesConverter converter = new UuidFacesConverter();
        FacesContext context = mock(FacesContext.class); UIComponent component = mock(UIComponent.class);
        UUID id = UUID.randomUUID();
        assertEquals(id, converter.getAsObject(context, component, id.toString()));
        assertNull(converter.getAsObject(context, component, null));
        assertNull(converter.getAsObject(context, component, ""));
        assertNull(converter.getAsObject(context, component, "invalid"));
        assertEquals(id.toString(), converter.getAsString(context, component, id));
        assertEquals("", converter.getAsString(context, component, null));
    }

    @Test void entityConverterRoundTripsObjectsThroughViewMap() {
        EntityConverter converter = new EntityConverter();
        FacesContext context = mock(FacesContext.class); UIViewRoot root = mock(UIViewRoot.class);
        UIComponent component = mock(UIComponent.class); Map<String,Object> view = new HashMap<>();
        when(context.getViewRoot()).thenReturn(root); when(root.getViewMap()).thenReturn(view);
        Object entity = new Object();
        try {
            TestFacesContext.install(context);
            String key = converter.getAsString(context, component, entity);
            assertSame(entity, converter.getAsObject(context, component, key));
            assertNull(converter.getAsObject(context, component, null));
            assertNull(converter.getAsObject(context, component, "  "));
            assertEquals("", converter.getAsString(context, component, null));
        } finally { TestFacesContext.install(null); }
    }

    @Test void tipoCaracteristicaConverterReturnsEntityForExistingIdAndNullForBlank() {
        TipoCaracteristicaConverter converter = new TipoCaracteristicaConverter();
        TipoCaracteristicaDAOInterface dao = mock(TipoCaracteristicaDAOInterface.class);
        try {
            Field field = TipoCaracteristicaConverter.class.getDeclaredField("tipoCaracteristicaDAO");
            field.setAccessible(true); field.set(converter, dao);
        } catch (ReflectiveOperationException e) { throw new AssertionError(e); }
        FacesContext context = mock(FacesContext.class); UIComponent component = mock(UIComponent.class);
        UUID id = UUID.randomUUID(); TipoCaracteristica entity = new TipoCaracteristica();
        when(dao.find(id)).thenReturn(entity);
        assertSame(entity, converter.getAsObject(context, component, id.toString()));
        assertNull(converter.getAsObject(context, component, null));
        assertNull(converter.getAsObject(context, component, "  "));
        assertNull(converter.getAsObject(context, component, "bad"));
        assertEquals("", converter.getAsString(context, component, null));
        entity.setIdTipoCaracteristica(id);
        assertEquals(id.toString(), converter.getAsString(context, component, entity));
    }
}
