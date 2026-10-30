# Extractos de código para revisión

Extractos tomados del proyecto `cafefe`; se conservaron solo las partes relacionadas con guardado/validación, persistencia, mapeo JPA y dependencias.

## `ProductoBean.java`: listado, guardado y validaciones

```java
public List<Producto> getLista() {
    return dao.findRange(0, 100);
}

public void guardar() {
    if (registro == null) return;

    if (!esTipoProductoValido()) {
        mensajeError("Debe seleccionar un tipo de producto activo.");
        return;
    }
    if (!esCaracteristicaValida()) {
        mensajeError("El valor ingresado no cumple con la expresión regular requerida.");
        return;
    }
    if (!esDescuentoValido()) {
        return;
    }

    prepararYGuardarProducto();
    mensajeInfo("Producto guardado correctamente.");
    limpiarFormulario();
}

private boolean esTipoProductoValido() {
    return tipoProductoSeleccionado != null
            && Boolean.TRUE.equals(tipoProductoSeleccionado.getActivo());
}

private boolean esCaracteristicaValida() {
    if (caracteristicaSeleccionada == null
            || caracteristicaSeleccionada.getIdTipoCaracteristica() == null) {
        return true;
    }
    String regex = caracteristicaSeleccionada.getIdTipoCaracteristica().getExpresionRegular();
    if (regex == null || regex.isBlank()) return true;
    return valorCaracteristica != null && Pattern.matches(regex, valorCaracteristica);
}
```

## `ProductoBean.java`: validar descuento y persistir relaciones

```java
private boolean esDescuentoValido() {
    if (descuentoSeleccionado == null) return true;

    TipoDescuento td = descuentoSeleccionado.getIdTipoDescuento();
    if (td != null && td.getDescuentoMaximo() != null && valorDescuento != null
            && valorDescuento > td.getDescuentoMaximo()) {
        mensajeError("El descuento " + valorDescuento
                + "% supera el máximo permitido (" + td.getDescuentoMaximo() + "%).");
        return false;
    }

    Date hoy = new Date();
    if (descuentoSeleccionado.getFechaDesde() != null
            && hoy.before(descuentoSeleccionado.getFechaDesde())) {
        mensajeError("El descuento seleccionado aún no ha iniciado su vigencia.");
        return false;
    }
    if (descuentoSeleccionado.getFechaHasta() != null
            && hoy.after(descuentoSeleccionado.getFechaHasta())) {
        mensajeError("El descuento seleccionado ya está vencido.");
        return false;
    }
    return true;
}

private void prepararYGuardarProducto() {
    if (registro.getIdProducto() == null) {
        registro.setIdProducto(UUID.randomUUID());
    }
    if (registro.getPrecioSugerido() == null) {
        registro.setPrecioSugerido(BigDecimal.ZERO);
    }

    ProductoTipoProducto ptp = new ProductoTipoProducto();
    ptp.setIdProductoTipoProducto(UUID.randomUUID());
    ptp.setIdProducto(registro);
    ptp.setIdTipoProducto(tipoProductoSeleccionado);
    ptp.setFechaCreacion(new Date());
    registro.setProductoTipoProductoList(List.of(ptp));

    if (caracteristicaSeleccionada != null) {
        ProductoCaracteristica pc = new ProductoCaracteristica();
        pc.setIdProductoCaracteristica(UUID.randomUUID());
        pc.setIdProducto(registro);
        pc.setIdCaracteristica(caracteristicaSeleccionada);
        pc.setValor(valorCaracteristica);
        registro.setProductoCaracteristicaList(List.of(pc));
    }

    if (descuentoSeleccionado != null) {
        DescuentoProducto dp = new DescuentoProducto();
        dp.setIdDescuentoProducto(UUID.randomUUID());
        dp.setIdProducto(registro);
        dp.setIdDescuento(descuentoSeleccionado);
        dp.setValor(valorDescuento);
        dp.setFechaDesde(descuentoSeleccionado.getFechaDesde());
        dp.setFechaHasta(descuentoSeleccionado.getFechaHasta());
        registro.setDescuentoProductoList(List.of(dp));
    }

    dao.create(registro);
}
```

> Nota: `ProductoBean` contiene la validación del tipo activo, pero el filtrado de tipos que se ve en la interfaz no aparece como método de este bean. La pantalla `Producto - Tipo` gestiona esa relación por separado.

## `DefaultDAO.java`: operaciones JPA

```java
@Override
public void create(T entity) {
    EntityManager em = getEntityManager();
    if (em == null) throw new IllegalStateException("EntityManager nulo.");
    if (entity == null) throw new IllegalArgumentException("La entidad a crear no puede ser nula.");
    em.persist(entity);
}

@Override
public T edit(T entity) {
    EntityManager em = getEntityManager();
    if (em == null) throw new IllegalStateException("EntityManager nulo.");
    if (entity == null) throw new IllegalArgumentException("La entidad a editar no puede ser nula.");
    return em.merge(entity);
}

@Override
public void remove(T entity) {
    EntityManager em = getEntityManager();
    if (em == null) throw new IllegalStateException("EntityManager nulo.");
    if (entity == null) throw new IllegalArgumentException("La entidad a eliminar no puede ser nula.");
    em.remove(em.merge(entity));
}
```

`DefaultDAO` no declara `@Transactional` ni inicia/termina transacciones manualmente; usa `EntityManager.persist/merge/remove`. El límite transaccional depende de cómo se proporcione el `EntityManager` y del contenedor.

## `Producto.java`: mapeo JPA

```java
@Entity
@Table(name = "producto")
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p"),
    @NamedQuery(name = "Producto.findByNombre",
                query = "SELECT p FROM Producto p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Producto.findByActivo",
                query = "SELECT p FROM Producto p WHERE p.activo = :activo")
})
public class Producto implements Serializable {
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_producto")
    private UUID idProducto;

    @Size(max = 155)
    @Column(name = "nombre")
    private String nombre;

    @NotNull
    @Column(name = "precio_sugerido", nullable = false, precision = 8, scale = 2)
    private BigDecimal precioSugerido;

    @Column(name = "activo")
    private Boolean activo;

    @OneToMany(mappedBy = "idProducto", fetch = FetchType.LAZY)
    private List<ProductoCaracteristica> productoCaracteristicaList;

    @OneToMany(mappedBy = "idProducto", fetch = FetchType.LAZY)
    private List<OrdenProducto> ordenProductoList;

    @OneToMany(mappedBy = "idProducto", fetch = FetchType.LAZY)
    private List<DescuentoProducto> descuentoProductoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProducto")
    private List<ProductoTipoProducto> productoTipoProductoList;
}
```

## `pom.xml`: dependencias

```xml
<dependencies>
    <dependency>
        <groupId>org.primefaces</groupId>
        <artifactId>primefaces</artifactId>
        <version>14.0.0</version>
        <classifier>jakarta</classifier>
    </dependency>
    <dependency>
        <groupId>jakarta.platform</groupId>
        <artifactId>jakarta.jakartaee-api</artifactId>
        <version>${jakartaee}</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.7.4</version>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.10.2</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.10.2</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>5.11.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-junit-jupiter</artifactId>
        <version>5.11.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```
