package domain.operaciones;

import domain.APIs.Api;

import javax.persistence.*;

@Entity
@Table(name = "Proveedores")
public class Proveedor {

    public String apellido;
    @Column(name = "cuil_o_cuit")
    public long cuilOcuit;
    @Embedded
    public Ubicacion ubicacion;
    private String nombre;
    @Id
    @GeneratedValue
    private Long id;

    public Proveedor() {
    }

    public Proveedor(Api api, NombrePais pais, String codigoPostal, String calle, int altura, int piso, String departamento, long cuilOcuit, String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cuilOcuit = cuilOcuit;
        this.ubicacion = new Ubicacion(codigoPostal, calle, altura, piso, departamento, pais, api);
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }
}
