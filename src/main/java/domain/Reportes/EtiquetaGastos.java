package domain.Reportes;

import java.math.BigDecimal;

public class EtiquetaGastos {

    String nombre;
    BigDecimal gastoMensual;

    public EtiquetaGastos(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreEtiqueta() {
        return nombre;
    }

    public BigDecimal getGastoMensual() {
        return gastoMensual;
    }

    public void setGastoMensual(BigDecimal gasto) {
        this.gastoMensual = gasto;
    }
}
