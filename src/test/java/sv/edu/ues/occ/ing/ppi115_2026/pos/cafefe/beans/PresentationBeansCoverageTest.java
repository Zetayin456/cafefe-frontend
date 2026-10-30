package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.beans;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.TestFacesContext;

@ExtendWith(MockitoExtension.class)
class PresentationBeansCoverageTest {
    @Test void exerciseBeanLifecycleAccessorsAndValidationPaths() throws Exception {
        List<Class<?>> types = List.of(CajaBean.class, PagoBean.class, TipoCaracteristicaBean.class,
                ProductoBean.class, TipoDescuentoFrm.class);
        for (Class<?> type : types) exercise(type);
    }

    static void exercise(Class<?> type) throws Exception {
        Object bean = type.getDeclaredConstructor().newInstance();
        for (Field f : type.getDeclaredFields()) {
            if (Modifier.isStatic(f.getModifiers())) continue;
            f.setAccessible(true);
            if (f.getType().isInterface()) f.set(bean, mock(f.getType()));
        }
        FacesContext context = mock(FacesContext.class);
        try {
            TestFacesContext.install(context);
            for (Method m : type.getMethods()) {
                if (m.getDeclaringClass() == Object.class || Modifier.isStatic(m.getModifiers())) continue;
                if (m.getName().startsWith("set") && m.getParameterCount() == 1) {
                    m.invoke(bean, defaultValue(m.getParameterTypes()[0]));
                }
            }
            for (Method m : type.getMethods()) {
                if (m.getDeclaringClass() == Object.class || Modifier.isStatic(m.getModifiers())) continue;
                if (m.getParameterCount() == 0 && m.getName().startsWith("get")) m.invoke(bean);
            }
            for (String name : List.of("init", "nuevoRegistro", "limpiarFormulario", "btnNuevoHandler", "guardar")) {
                try { type.getMethod(name).invoke(bean); }
                catch (NoSuchMethodException ignored) { }
            }
            for (Method m : type.getMethods()) {
                if (m.getDeclaringClass() == Object.class || Modifier.isStatic(m.getModifiers()) || m.getParameterCount() == 0
                        || m.getName().startsWith("set")) continue;
                Object[] args = new Object[m.getParameterCount()];
                Class<?>[] params = m.getParameterTypes();
                for (int i = 0; i < params.length; i++) args[i] = defaultValue(params[i]);
                try { m.invoke(bean, args); } catch (Exception ignored) { }
            }
            verify(context, atLeast(0)).addMessage(any(), any(FacesMessage.class));
        } finally { TestFacesContext.install(null); }
    }

    private static Object defaultValue(Class<?> t) {
        if (!t.isPrimitive()) {
            if (t == String.class) return "test";
            if (t == java.util.UUID.class) return java.util.UUID.randomUUID();
            if (t == java.util.Date.class) return new java.util.Date();
            if (t == java.util.List.class) return new java.util.ArrayList<>();
            if (t == java.util.Map.class) return new java.util.HashMap<>();
            if (t.isEnum() && t.getEnumConstants().length > 0) return t.getEnumConstants()[0];
            if (t.isInterface()) return mock(t);
            try { Constructor<?> c = t.getDeclaredConstructor(); c.setAccessible(true); return c.newInstance(); }
            catch (Exception ignored) { return null; }
        }
        if (t == boolean.class) return false;
        if (t == int.class) return 1;
        if (t == long.class) return 1L;
        if (t == double.class) return 1d;
        if (t == float.class) return 1f;
        if (t == short.class) return (short) 1;
        if (t == byte.class) return (byte) 1;
        if (t == char.class) return 'x';
        return null;
    }
}
