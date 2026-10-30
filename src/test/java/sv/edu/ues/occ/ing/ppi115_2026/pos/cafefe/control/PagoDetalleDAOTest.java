package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.PagoDetalle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PagoDetalleDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<PagoDetalle> query;

    @InjectMocks
    private PagoDetalleDAO pagoDetalleDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrear() {
        PagoDetalle entidad = new PagoDetalle();
        
        pagoDetalleDAO.crear(entidad);
        
        verify(em, times(1)).persist(entidad);
    }

    @Test
    public void testModificar() {
        PagoDetalle entidad = new PagoDetalle();
        
        pagoDetalleDAO.modificar(entidad);
        
        verify(em, times(1)).merge(entidad);
    }

    @Test
    public void testObtenerTodos() {
        List<PagoDetalle> listaEsperada = new ArrayList<>();
        listaEsperada.add(new PagoDetalle());

        when(em.createQuery("SELECT pd FROM PagoDetalle pd LEFT JOIN FETCH pd.idPago ORDER BY pd.idPagoDetalle", PagoDetalle.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(listaEsperada);

        List<PagoDetalle> resultado = pagoDetalleDAO.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(em, times(1)).createQuery("SELECT pd FROM PagoDetalle pd LEFT JOIN FETCH pd.idPago ORDER BY pd.idPagoDetalle", PagoDetalle.class);
    }
}
