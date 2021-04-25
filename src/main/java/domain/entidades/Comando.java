package domain.entidades;

public enum Comando {
    NO_ACEPTAR_MAS_EGRESOS {
        public String getDescripcion() {
            return "No hacer mas egresos";
        }

        public void ejecutar(Entidad entidad) {

            if (-1 == entidad.getMontoLimite().compareTo(entidad.getMontoAlcanzado())) {
                entidad.bloquearOdesbloquearParaComprar(true);
            }/*compara con el -1 porque el compareTo da 0 o 1 , si los valores son iguales o el segundo mayor*/
        }
    },
    NO_AGREGAR_MAS_ENTIDADES_BASE {
        public String getDescripcion() {
            return "No agregar mas entidades base a juridica";
        }

        public void ejecutar(Entidad entidad) {
            entidad.bloquearOdesbloquear(true);
        }
    },
    BLOQUEAR_ENTIDAD_BASE_PARA_JURIDICA {
        public String getDescripcion() {
            return "No agregar entidad base a ninguna juridica";
        }

        public void ejecutar(Entidad entidad) {
            entidad.bloquearOdesbloquear(true);
        }
    };

    public abstract void ejecutar(Entidad entidad);

    public abstract String getDescripcion();

}
