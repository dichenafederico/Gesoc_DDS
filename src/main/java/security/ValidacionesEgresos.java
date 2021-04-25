package security;

import domain.operaciones.Egreso;

public enum ValidacionesEgresos {

    VALIDACION_ITEM_EXISTENTE {
        public Boolean esValido(Egreso egreso) {
            return !egreso.getItems().stream().allMatch(item -> egreso.existeItemEnPresupuestos(item));
        }

        public String getMensajeValidacion() {
            return "Egreso no correctamente presupuestado";
        }
    },

    VALIDACION_PROVEEDOR_CORRECTO {
        public Boolean esValido(Egreso egreso) {
            return !egreso.criterio.proveedorEsperado(egreso);
        }

        public String getMensajeValidacion() {

            return "Egreso con Proveedor de Menor Valor";
        }
    },

    VALIDACION_CANTIDAD_PRESUPUESTOS {
        public Boolean esValido(Egreso egreso) {
            return egreso.getCantidadPresupuestos() < cantidadPresupuestosNecesarios;
        }

        public String getMensajeValidacion() {

            return "El egreso no tiene la cantidad de presupuestos requerida";
        }
    };

    private static final int cantidadPresupuestosNecesarios = 6;

    public abstract Boolean esValido(Egreso egreso);

    public abstract String getMensajeValidacion();
}
