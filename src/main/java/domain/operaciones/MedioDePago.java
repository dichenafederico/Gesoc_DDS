package domain.operaciones;

import javax.persistence.*;

@Entity
@Table(name = "Medios_de_pago")
public class MedioDePago {
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tipo_de_pago")
    public TipoDePago tipoDePago;
    public String idMercadoPago;
    @Id
    @GeneratedValue
    private Long id;
    private String nombre;

    public MedioDePago() {
    }

    public MedioDePago(String nombre, TipoDePago tipo) {
        this.nombre = nombre;
        this.tipoDePago = tipo;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

}

