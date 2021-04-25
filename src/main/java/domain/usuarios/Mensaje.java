package domain.usuarios;

import domain.operaciones.Egreso;

import javax.persistence.*;

@Entity
@Table(name = "Mensajes")
public class Mensaje {

    @ManyToOne
    private Egreso egresoError;
    private String descripcion;
    @Id
    @GeneratedValue
    private Long id;

    public Mensaje() {
    }

    public Mensaje(String mensaje, Egreso egreso) {
        this.egresoError = egreso;
        this.descripcion = mensaje;
    }

    public Egreso getEgresoError() {
        return egresoError;
    }

    public Long getId() {
        return id;
    }

    public String getDescripcion() {
        return this.descripcion;
    }
}
