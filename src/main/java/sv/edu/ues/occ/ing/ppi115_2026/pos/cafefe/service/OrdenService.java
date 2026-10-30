package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.UUID;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control.EmpleadoRolDAOInterface;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control.OrdenDAOInterface;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control.ProductoDAOInterface;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.EmpleadoRol;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.Orden;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.OrdenProducto;
import sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.Producto;

/** Reglas y operaciones de aplicación para el flujo de órdenes. */
@Stateless
public class OrdenService {

    @Inject
    private OrdenDAOInterface ordenDAO;

    @Inject
    private EmpleadoRolDAOInterface empleadoRolDAO;

    @Inject
    private ProductoDAOInterface productoDAO;

    public List<Orden> listar() {
        List<Orden> ordenes = ordenDAO.findAllWithDetails();
        Map<UUID, Producto> productosConDescuentos = new HashMap<>();
        for (Producto producto : productoDAO.findActivosConDescuentos()) {
            productosConDescuentos.put(producto.getIdProducto(), producto);
        }
        for (Orden orden : ordenes) {
            if (orden.getOrdenProductoList() == null) continue;
            for (OrdenProducto detalle : orden.getOrdenProductoList()) {
                if (detalle.getIdProducto() == null) continue;
                Producto producto = productosConDescuentos.get(detalle.getIdProducto().getIdProducto());
                if (producto != null) detalle.setIdProducto(producto);
            }
        }
        return ordenes;
    }

    public List<Producto> listarProductosDisponibles() {
        return productoDAO.findActivosConDescuentos();
    }

    public List<EmpleadoRol> listarCamarerosDisponibles() {
        List<EmpleadoRol> camareros = new ArrayList<>();
        for (EmpleadoRol asignacion : empleadoRolDAO.findAllWithRelations()) {
            if (asignacion != null && Boolean.TRUE.equals(asignacion.getActivo())
                    && asignacion.getIdRol() != null
                    && Boolean.TRUE.equals(asignacion.getIdRol().getActivo())
                    && "CAMARERO".equalsIgnoreCase(asignacion.getIdRol().getNombre())) {
                camareros.add(asignacion);
            }
        }
        return camareros;
    }

    public void guardar(Orden orden, List<OrdenProducto> productos) {
        if (orden == null) throw new IllegalArgumentException("La orden es obligatoria.");
        if (orden.getFechaCreacion() == null) throw new IllegalArgumentException("La fecha de la orden es obligatoria.");
        if (orden.getIdEmpleadoRol() == null || orden.getIdEmpleadoRol().getIdEmpleadoRol() == null) {
            throw new IllegalArgumentException("Selecciona una asignación activa del rol Camarero.");
        }
        EmpleadoRol asignacion = empleadoRolDAO.find(orden.getIdEmpleadoRol().getIdEmpleadoRol());
        if (asignacion == null || !Boolean.TRUE.equals(asignacion.getActivo())
                || asignacion.getIdRol() == null
                || !Boolean.TRUE.equals(asignacion.getIdRol().getActivo())
                || !"CAMARERO".equalsIgnoreCase(asignacion.getIdRol().getNombre())) {
            throw new IllegalArgumentException("Selecciona una asignación activa del rol Camarero.");
        }
        orden.setIdEmpleadoRol(asignacion);
        if (productos == null || productos.isEmpty()) throw new IllegalArgumentException("Agrega al menos un producto.");

        boolean nueva = orden.getIdOrden() == null;
        if (nueva) orden.setIdOrden(UUID.randomUUID());
        for (OrdenProducto detalle : productos) {
            if (detalle == null || detalle.getIdProducto() == null || detalle.getIdProducto().getIdProducto() == null) {
                throw new IllegalArgumentException("Todos los productos deben estar activos y seleccionados.");
            }
            Producto producto = productoDAO.find(detalle.getIdProducto().getIdProducto());
            if (producto == null || !Boolean.TRUE.equals(producto.getActivo())) {
                throw new IllegalArgumentException("Todos los productos deben estar activos y seleccionados.");
            }
            if (detalle.getPrecio() == null || detalle.getPrecio().signum() < 0) {
                throw new IllegalArgumentException("El precio de cada producto debe ser mayor o igual a cero.");
            }
            if (detalle.getIdOrdenProducto() == null) detalle.setIdOrdenProducto(UUID.randomUUID());
            detalle.setIdProducto(producto);
            detalle.setIdOrden(orden);
        }
        orden.setOrdenProductoList(new ArrayList<>(productos));
        if (nueva) ordenDAO.crear(orden);
        else ordenDAO.modificar(orden);
    }

    public void eliminar(Orden orden) {
        if (orden == null || orden.getIdOrden() == null) throw new IllegalArgumentException("Selecciona una orden válida.");
        ordenDAO.eliminar(orden);
    }
}
