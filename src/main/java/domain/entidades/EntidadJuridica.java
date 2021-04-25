package domain.entidades;

import domain.Organizacion;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Entidades_juridicas")
public class EntidadJuridica extends Entidad {

    @Column(name = "direccion_postal")
    public int direccionPostal;
    public long cuit;
    @Column(name = "razon_social")
    public String razonSocial;
    @Column(name = "codigo_inscripcion_en_igj")
    public long codigoInscripcionEnIGJ;
    @OneToMany
    @JoinColumn(name = "entidadJuridica_id")
    public List<EntidadBase> entidadesBase = new ArrayList<>();
    @Transient
    public Categoria categoriaReal;
    @Embedded

    public Empresa categoria;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "tipo", column = @Column(name = "tipoEntidadJuridica"))
    })
    public Empresa categoriaEntidadJuridica;

    public EntidadJuridica() {
    }

    public EntidadJuridica(String nombre, Organizacion organizacion, BigDecimal montoLimite) {
        this.montoLimite = montoLimite;
        this.nombreFicticio = nombre;
        this.organizacion = organizacion;
        organizacion.agregarEntidad(this);
    }

    public void agregarEntidadBase(EntidadBase entidad) {
        if (this.bloqueadaOno()) { /*En ambos casos , no se si correspondia tirar una excepcion porque es una accion
         que debe poder hacerse , no es un error de usuario , solo una restriccion que puede estar o no */
            System.out.println("Esta entidad juridica esta bloqueada , por eso no se le agrego la entidad");
        } else {
            if (entidad.bloqueadaOno()) {
                System.out.println("La entidad que quiere agregar esta bloqueada , por eso no se le agrego");
            } else {
                this.entidadesBase.add(entidad);
            }
        }

    }

    public List<EntidadBase> getEntidadesBase() {
        return entidadesBase;
    }

    public void setCuit(long cuit) {
        this.cuit = cuit;
    }

    public void setCodigoInscripcionEnIGJ(long codigoInscripcionEnIGJ) {
        this.codigoInscripcionEnIGJ = codigoInscripcionEnIGJ;
    }

    public void setDireccionPostal(int direccionPostal) {
        this.direccionPostal = direccionPostal;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getTipo() {
        return "Entidad juridica";
    }
}
