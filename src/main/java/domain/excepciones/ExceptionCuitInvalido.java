package domain.excepciones;

public class ExceptionCuitInvalido extends RuntimeException {
    public ExceptionCuitInvalido(String mensaje) {
        super(mensaje);
    }
}
