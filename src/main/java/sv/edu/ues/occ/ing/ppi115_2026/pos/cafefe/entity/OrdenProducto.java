package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orden_producto")
@NamedQueries({
    @NamedQuery(name = "OrdenProducto.findAll", query = "SELECT o FROM OrdenProducto o"),
    @NamedQuery(name = "OrdenProducto.findByPrecio", query = "SELECT o FROM OrdenProducto o WHERE o.precio = :precio"),
    @NamedQuery(name = "OrdenProducto.findByObservaciones", query = "SELECT o FROM OrdenProducto o WHERE o.observaciones = :observaciones")})
public class OrdenProducto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_orden_producto")
    private UUID idOrdenProducto;

    @Size(max = 2147483647)
    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "precio")
    private BigDecimal precio;

    @Transient
    private UUID idDescuentoProductoSeleccionado;

    @OneToMany(mappedBy = "idOrdenProducto", fetch = FetchType.LAZY)
    private List<FacturaOrdenProducto> facturaOrdenProductoList;

    @JoinColumn(name = "id_orden", referencedColumnName = "id_orden")
    @ManyToOne(fetch = FetchType.LAZY)
    private Orden idOrden;

    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto")
    @ManyToOne(fetch = FetchType.LAZY)
    private Producto idProducto;

    public OrdenProducto() {
    }

    public OrdenProducto(UUID idOrdenProducto) {
        this.idOrdenProducto = idOrdenProducto;
    }

    public UUID getIdOrdenProducto() {
        return idOrdenProducto;
    }

    public void setIdOrdenProducto(UUID idOrdenProducto) {
        this.idOrdenProducto = idOrdenProducto;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public UUID getIdDescuentoProductoSeleccionado() {
        return idDescuentoProductoSeleccionado;
    }

    public void setIdDescuentoProductoSeleccionado(UUID idDescuentoProductoSeleccionado) {
        this.idDescuentoProductoSeleccionado = idDescuentoProductoSeleccionado;
    }

    public List<FacturaOrdenProducto> getFacturaOrdenProductoList() {
        return facturaOrdenProductoList;
    }

    public void setFacturaOrdenProductoList(List<FacturaOrdenProducto> facturaOrdenProductoList) {
        this.facturaOrdenProductoList = facturaOrdenProductoList;
    }

    public Orden getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(Orden idOrden) {
        this.idOrden = idOrden;
    }

    public Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOrdenProducto != null ? idOrdenProducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrdenProducto)) {
            return false;
        }
        OrdenProducto other = (OrdenProducto) object;
        if ((this.idOrdenProducto == null && other.idOrdenProducto != null) || (this.idOrdenProducto != null && !this.idOrdenProducto.equals(other.idOrdenProducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity.OrdenProducto[ idOrdenProducto=" + idOrdenProducto + " ]";
    }
}
