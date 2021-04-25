package domain.operaciones;

import javax.persistence.*;

@Entity(name = "Ciudades")
@Table(name = "Ciudades")
public class Ciudad {
    public String nombre;
    @Column(name = "Ciudad_Id")
    public String ciudadId;
    @Id
    @GeneratedValue
    private Long id;

    public Ciudad() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return ciudadId;
    }

    public void setId(String id) {
        this.ciudadId = id;
    }
}
