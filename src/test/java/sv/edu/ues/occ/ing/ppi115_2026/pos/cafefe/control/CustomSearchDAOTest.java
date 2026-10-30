package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.EmpleadoRol;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.Producto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomSearchDAOTest {
    @Mock EntityManager em;
    private static void inject(Object target, EntityManager em) throws Exception {
        Field field = target.getClass().getDeclaredField("em"); field.setAccessible(true); field.set(target, em);
    }
    @Test void productSearchesReturnActiveAndDiscountLinkedResults() throws Exception {
        ProductoDAO dao = new ProductoDAO(); inject(dao, em);
        @SuppressWarnings("unchecked") TypedQuery<Producto> query = mock(TypedQuery.class);
        List<Producto> products = List.of(new Producto());
        when(em.createQuery(anyString(), eq(Producto.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(products);
        assertSame(products, dao.findActivos()); assertSame(products, dao.findActivosConDescuentos());
        verify(em, times(2)).createQuery(anyString(), eq(Producto.class)); verify(query, times(2)).getResultList();
    }

    @Test void employeeRoleSearchHandlesNullSuccessAndQueryFailure() throws Exception {
        EmpleadoRolDAO dao = new EmpleadoRolDAO(); inject(dao, em);
        assertTrue(dao.findByEmpleado(null).isEmpty()); verifyNoInteractions(em);

        @SuppressWarnings("unchecked") TypedQuery<EmpleadoRol> query = mock(TypedQuery.class);
        List<EmpleadoRol> links = List.of(new EmpleadoRol());
        when(em.createQuery(anyString(), eq(EmpleadoRol.class))).thenReturn(query);
        when(query.setParameter(eq("idEmpleado"), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(links);
        assertSame(links, dao.findByEmpleado(UUID.randomUUID()));
        assertSame(links, dao.findAllWithRelations());

        when(em.createQuery(anyString(), eq(EmpleadoRol.class))).thenThrow(new IllegalStateException("query error"));
        assertTrue(dao.findByEmpleado(UUID.randomUUID()).isEmpty());
        assertThrows(IllegalStateException.class, dao::findAllWithRelations);
    }
}
