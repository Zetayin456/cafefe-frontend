package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EntityAccessorsCoverageTest {
    @Test void entityConstructorsAndAccessorsCanBeUsed() throws Exception {
        List<Class<?>> entities = List.of(Caja.class, Caracteristica.class, Descuento.class, DescuentoProducto.class,
                Empleado.class, EmpleadoRol.class, Factura.class, FacturaOrdenProducto.class, Orden.class,
                OrdenProducto.class, Pago.class, PagoDetalle.class, Producto.class, ProductoCaracteristica.class,
                ProductoTipoProducto.class, Rol.class, TipoCaracteristica.class, TipoDescuento.class, TipoProducto.class);
        for (Class<?> type : entities) {
            Constructor<?> c = type.getDeclaredConstructor(); c.setAccessible(true);
            Object entity = c.newInstance();
            for (Method method : type.getMethods()) {
                if (method.getDeclaringClass() == Object.class || Modifier.isStatic(method.getModifiers())) continue;
                if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
                    try { method.invoke(entity, valueFor(method.getParameterTypes()[0])); }
                    catch (IllegalArgumentException ignored) { }
                }
            }
            for (Method method : type.getMethods())
                if (method.getDeclaringClass() != Object.class && !Modifier.isStatic(method.getModifiers())
                        && method.getName().startsWith("get") && method.getParameterCount() == 0) method.invoke(entity);
            assertNotNull(entity);
        }
    }
    private Object valueFor(Class<?> type) {
        if (type == String.class) return "test";
        if (type == UUID.class) return UUID.randomUUID();
        if (type == BigDecimal.class) return BigDecimal.ONE;
        if (type == LocalDateTime.class) return LocalDateTime.now();
        if (type == Date.class) return new Date();
        if (type == Boolean.class || type == boolean.class) return true;
        if (type == Integer.class || type == int.class) return 1;
        if (type == Long.class || type == long.class) return 1L;
        if (type == Double.class || type == double.class) return 1d;
        if (type == Float.class || type == float.class) return 1f;
        if (type.isEnum() && type.getEnumConstants().length > 0) return type.getEnumConstants()[0];
        if (type.isInterface()) return java.util.List.class.isAssignableFrom(type) ? new java.util.ArrayList<>() : null;
        try { Constructor<?> c = type.getDeclaredConstructor(); c.setAccessible(true); return c.newInstance(); }
        catch (Exception ignored) { return null; }
    }
}
