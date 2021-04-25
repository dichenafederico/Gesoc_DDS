package domain.operaciones;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Provincias")
@Table(name = "Provincias")
public class Provincia {

    public String nombre;
    @Column(name = "Provincia_Id")
    public String provinciaId;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "provincia_id")
    public List<Ciudad> ciudades = new ArrayList<>();
    @Id
    @GeneratedValue
    private Long id;

    public Provincia() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return provinciaId;
    }

    public void setId(String id) {
        this.provinciaId = id;
    }

    public void agregarCiudad(Ciudad ciudad) {
        this.ciudades.add(ciudad);
    }

    public List<Ciudad> getCiudades() {
        return this.ciudades;
    }

}
