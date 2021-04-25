package domain.operaciones;

import domain.excepciones.ExceptionFaltaParametros;
import domain.excepciones.ExceptionValorItemInvalido;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Items")
public class Item {

    public String descripcion;
    @Embedded
    public Dinero valor;
    @Id
    @GeneratedValue
    private Long id;

    public Item() {
    }

    public Item(String descripcion) {
        if (descripcion.isEmpty())
            throw new ExceptionFaltaParametros("El item debe tener una descripción");
        this.descripcion = descripcion;
    }

    public BigDecimal getValor() {
        return valor.getCantidad();
    }

    public void setValor(BigDecimal valor) {
        if (valor.compareTo(new BigDecimal(0)) == 0)
            throw new ExceptionValorItemInvalido("El valor del item debe ser mayor a 0");
        if (this.valor == null) {
            this.valor = new Dinero(valor, NombreDivisa.PESO_ARGENTINO);
        } else {
            this.valor.setCantidad(valor);
        }
    }

    public Dinero getMoneda() {
        return valor;
    }
}
