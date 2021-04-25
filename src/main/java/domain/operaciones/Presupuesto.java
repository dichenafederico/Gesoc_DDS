package domain.operaciones;

import domain.excepciones.ExceptionPresupuestoSinEgreso;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Presupuestos")
public class Presupuesto implements Comparable<Presupuesto> {

    @OneToOne
    public DocumentoComercial documentoAsociadoPresupuesto;
    @OneToMany
    @JoinColumn(name = "presupuesto_id")
    public List<Item> items = new ArrayList<>();
    @ManyToOne
    public Egreso egresoAsociado;

    @Embedded
    public Dinero montoTotal = new Dinero(new BigDecimal(0.0), NombreDivisa.PESO_ARGENTINO);  /*Loo inicializa momentaneamente para que no rompa tests */
    @Id
    @GeneratedValue
    private Long id;

    //public Presupuesto(){}

    public Presupuesto(DocumentoComercial documentoAsociado, List<Item> items, Egreso egresoAsociado) {
        validarEgreso(egresoAsociado);
        this.egresoAsociado = egresoAsociado;
        //this.egresoAsociado.agregarPresupuesto(this);
        this.documentoAsociadoPresupuesto = documentoAsociado;
        this.items = items;
        calcularMontoTotal();
    }

    public void calcularMontoTotal() {
        this.items.forEach(item -> setMontoTotal(getMontoTotal().add(item.getValor())));
    }

    @Override
    public int compareTo(Presupuesto presupuesto) {
        return this.getMontoTotal().compareTo(presupuesto.getMontoTotal());
    }

    public BigDecimal getMontoTotal() {
        return montoTotal.getCantidad();
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal.setCantidad(montoTotal);
    }

    public void validarEgreso(Egreso egreso) {
        if (egreso == null)
            throw new ExceptionPresupuestoSinEgreso("El ingreso del egreso es obligatorio");
    }

    public boolean existeItem(Item itemEgreso) {
        return this.items.stream().anyMatch(item -> compararItems(itemEgreso, item));
    }

    public boolean compararItems(Item itemEgreso, Item itemPresupuesto) {
        return itemEgreso.descripcion == itemPresupuesto.descripcion && itemEgreso.getValor() == itemPresupuesto.getValor();
    }

}
