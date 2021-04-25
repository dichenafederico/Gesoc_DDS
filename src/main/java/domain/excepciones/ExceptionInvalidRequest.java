package domain.excepciones;

public class ExceptionInvalidRequest extends RuntimeException {
    public ExceptionInvalidRequest(String mensaje) {
        super(mensaje);
    }
}
