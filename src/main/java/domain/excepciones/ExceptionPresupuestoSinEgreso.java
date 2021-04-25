package domain.excepciones;

public class ExceptionPresupuestoSinEgreso extends RuntimeException {
    public ExceptionPresupuestoSinEgreso(String message) {
        super(message);
    }
}

