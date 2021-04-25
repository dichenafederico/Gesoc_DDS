package domain.entidades;

import domain.Organizacion;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "Entidades_Base")
public class EntidadBase extends Entidad {

    public String descripcion;

    public EntidadBase() {
    }

    public EntidadBase(String nombre, Organizacion organizacion, BigDecimal montoLimite) {
        this.montoLimite = montoLimite;
        this.nombreFicticio = nombre;
        this.organizacion = organizacion;
        this.organizacion.agregarEntidad(this);
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return "Entidad base";
    }
}
