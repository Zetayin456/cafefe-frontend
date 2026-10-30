package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.Descuento;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.DescuentoProducto;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.OrdenProducto;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.Producto;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.TipoDescuento;

import static org.junit.jupiter.api.Assertions.*;

class DescuentoServiceTest {

    private final DescuentoService service = new DescuentoService();
    private final Date fechaOrden = Date.from(Instant.parse("2026-10-27T12:00:00Z"));

    @Test
    void disponiblesFiltraPorVigenciaTipoActivoYMaximo() {
        DescuentoProducto valido = asignacion("Promoción", 15, true, 20, fecha("2026-10-01T00:00:00Z"), fecha("2026-10-31T23:59:59Z"));
        DescuentoProducto tipoInactivo = asignacion("Inactivo", 10, false, 20, null, null);
        DescuentoProducto excedeMaximo = asignacion("Excede", 25, true, 20, null, null);
        DescuentoProducto expirado = asignacion("Vencido", 10, true, 20, fecha("2026-10-01T00:00:00Z"), fecha("2026-10-26T23:59:59Z"));
        Producto producto = productoCon(valido, tipoInactivo, excedeMaximo, expirado);

        assertEquals(List.of(valido), service.disponibles(producto, fechaOrden));
        assertTrue(service.disponibles(producto, null).isEmpty());
    }

    @Test
    void aplicarUsaElDescuentoSeleccionadoYCalculaPrecioRedondeado() {
        DescuentoProducto seleccionado = asignacion("Promoción", 15, true, 20, null, null);
        Producto producto = productoCon(seleccionado);
        producto.setPrecioSugerido(new BigDecimal("19.99"));
        OrdenProducto item = new OrdenProducto();
        item.setIdProducto(producto);

        service.aplicar(item, seleccionado.getIdDescuentoProducto(), fechaOrden);

        assertEquals(new BigDecimal("16.99"), item.getPrecio());
        assertTrue(item.getObservaciones().contains("Promoción"));
        assertTrue(item.getObservaciones().contains("15%"));
    }

    @Test
    void aplicarRechazaUnaAsignacionQueNoPerteneceAlProducto() {
        OrdenProducto item = new OrdenProducto();
        item.setIdProducto(productoCon());

        assertThrows(IllegalArgumentException.class,
                () -> service.aplicar(item, UUID.randomUUID(), fechaOrden));
    }

    private Producto productoCon(DescuentoProducto... asignaciones) {
        Producto producto = new Producto(UUID.randomUUID());
        producto.setActivo(true);
        producto.setPrecioSugerido(new BigDecimal("10.00"));
        producto.setDescuentoProductoList(List.of(asignaciones));
        return producto;
    }

    private DescuentoProducto asignacion(String nombre, int valor, boolean tipoActivo,
            int maximo, Date desde, Date hasta) {
        TipoDescuento tipo = new TipoDescuento(UUID.randomUUID());
        tipo.setActivo(tipoActivo);
        tipo.setDescuentoMaximo(maximo);
        Descuento descuento = new Descuento(UUID.randomUUID());
        descuento.setNombre(nombre);
        descuento.setIdTipoDescuento(tipo);
        DescuentoProducto asignacion = new DescuentoProducto(UUID.randomUUID());
        asignacion.setValor(valor);
        asignacion.setFechaDesde(desde);
        asignacion.setFechaHasta(hasta);
        asignacion.setIdDescuento(descuento);
        return asignacion;
    }

    private Date fecha(String iso) { return Date.from(Instant.parse(iso)); }
}
