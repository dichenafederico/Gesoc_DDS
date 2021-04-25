package domain.operaciones;

import domain.repositorios.RepositorioDivisas;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Embeddable
public class Dinero {
    @ManyToOne(cascade = CascadeType.ALL)
    public Divisa tipo;
    private BigDecimal cantidad;

    public Dinero() {
    }

    public Dinero(BigDecimal cantidad, NombreDivisa tipo) {
        setCantidad(cantidad);
        this.tipo = RepositorioDivisas.getInstance().getDivisa(tipo);
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal valor) {
        cantidad = valor;
    }

    public Divisa getDivisa() {
        return tipo;
    }
}
