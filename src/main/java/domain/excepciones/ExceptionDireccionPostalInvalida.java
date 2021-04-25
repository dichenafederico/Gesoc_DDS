package domain.excepciones;

public class ExceptionDireccionPostalInvalida extends RuntimeException {
    public ExceptionDireccionPostalInvalida(String mensaje) {
        super(mensaje);
    }
}
