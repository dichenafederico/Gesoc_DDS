package domain.operaciones;

import java.math.BigDecimal;
import java.util.Collections;

public enum CriteriosSeleccionProveedor {

    MENORVALOR {
        @Override
        public boolean proveedorEsperado(Egreso egreso) {
            return egreso.getPresupuestoSeleccionado() == null || egreso.presupuestoSeleccionado.getMontoTotal().compareTo(valorDePresupuestoDeMenorValor(egreso)) == 0;
        }
    };

    public BigDecimal valorDePresupuestoDeMenorValor(Egreso egreso) {
        Collections.sort(egreso.getPresupuestos());
        return egreso.getPresupuestos().get(0).getMontoTotal();
    }

    public abstract boolean proveedorEsperado(Egreso egreso);
}
