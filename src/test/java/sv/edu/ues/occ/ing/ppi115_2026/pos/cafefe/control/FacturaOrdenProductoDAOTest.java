package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control;

import java.util.UUID;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.FacturaOrdenProducto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FacturaOrdenProductoDAOTest {

    @Mock
    private EntityManager em;

    @InjectMocks
    private FacturaOrdenProductoDAO facturaOrdenProductoDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate() {
        FacturaOrdenProducto entidad = new FacturaOrdenProducto();
        entidad.setIdFacturaOrdenProducto(UUID.randomUUID());
        facturaOrdenProductoDAO.crear(entidad);
        verify(em, times(1)).persist(entidad);
    }

    @Test
    public void testEdit() {
        FacturaOrdenProducto entidad = new FacturaOrdenProducto();
        entidad.setIdFacturaOrdenProducto(UUID.randomUUID());
        facturaOrdenProductoDAO.modificar(entidad);
        verify(em, times(1)).merge(entidad);
    }

    @Test
    public void testRemove() {
        FacturaOrdenProducto entidad = new FacturaOrdenProducto();
        entidad.setIdFacturaOrdenProducto(UUID.randomUUID());

        when(em.find(FacturaOrdenProducto.class, entidad.getIdFacturaOrdenProducto())).thenReturn(entidad);

        facturaOrdenProductoDAO.eliminar(entidad);

        verify(em, times(1)).find(FacturaOrdenProducto.class, entidad.getIdFacturaOrdenProducto());
        verify(em, times(1)).remove(entidad);
    }
}
