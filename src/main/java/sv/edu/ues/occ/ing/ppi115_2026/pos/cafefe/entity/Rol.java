
package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rol")
@NamedQueries({
    @NamedQuery(name = "Rol.findAll",
            query = "SELECT r FROM Rol r"),
    @NamedQuery(name = "Rol.findByNombre",
            query = "SELECT r FROM Rol r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "Rol.findByActivo",
            query = "SELECT r FROM Rol r WHERE r.activo = :activo"),
    @NamedQuery(name = "Rol.findByObservaciones",
            query = "SELECT r FROM Rol r WHERE r.observaciones = :observaciones")
})
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_rol")
    private UUID idRol;

    @Size(max = 155)
    @Column(name = "nombre")
    private String nombre;

    @Size(max = 2147483647)
    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "activo")
    private Boolean activo;

    @OneToMany(mappedBy = "idRol", fetch = FetchType.LAZY)
    private List<EmpleadoRol> empleadoRolList;

    public Rol() {
    }

    public Rol(UUID idRol) {
        this.idRol = idRol;
    }

    public UUID getIdRol() {
        return idRol;
    }

    public void setIdRol(UUID idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<EmpleadoRol> getEmpleadoRolList() {
        return empleadoRolList;
    }

    public void setEmpleadoRolList(List<EmpleadoRol> empleadoRolList) {
        this.empleadoRolList = empleadoRolList;
    }

    @Override
    public int hashCode() {
        return idRol != null ? idRol.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Rol)) {
            return false;
        }

        Rol other = (Rol) object;

        return idRol != null && idRol.equals(other.idRol);
    }

    @Override
    public String toString() {
        return "Rol[idRol=" + idRol + "]";
    }
}
