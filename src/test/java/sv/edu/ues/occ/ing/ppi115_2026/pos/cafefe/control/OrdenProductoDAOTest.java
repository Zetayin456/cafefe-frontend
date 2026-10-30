package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control;

import java.util.UUID;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.OrdenProducto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrdenProductoDAOTest {

    @Mock
    private EntityManager em;

    @InjectMocks
    private OrdenProductoDAO ordenProductoDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrear() {
        OrdenProducto entidad = new OrdenProducto();
        entidad.setIdOrdenProducto(UUID.randomUUID());

        ordenProductoDAO.crear(entidad);

        verify(em, times(1)).persist(entidad);
    }

    @Test
    public void testBuscarPorId() {
        UUID id = UUID.randomUUID();
        OrdenProducto esperada = new OrdenProducto();
        esperada.setIdOrdenProducto(id);

        when(em.find(OrdenProducto.class, id)).thenReturn(esperada);

        OrdenProducto resultado = ordenProductoDAO.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getIdOrdenProducto());
    }

    @Test
    public void testModificar() {
        OrdenProducto entidad = new OrdenProducto();
        entidad.setIdOrdenProducto(UUID.randomUUID());

        ordenProductoDAO.modificar(entidad);

        verify(em, times(1)).merge(entidad);
    }

    @Test
    public void testEliminar() {
        OrdenProducto entidad = new OrdenProducto();
        entidad.setIdOrdenProducto(UUID.randomUUID());

        when(em.find(OrdenProducto.class, entidad.getIdOrdenProducto())).thenReturn(entidad);

        ordenProductoDAO.eliminar(entidad);

        verify(em, times(1)).find(OrdenProducto.class, entidad.getIdOrdenProducto());
        verify(em, times(1)).remove(entidad);
    }
}
