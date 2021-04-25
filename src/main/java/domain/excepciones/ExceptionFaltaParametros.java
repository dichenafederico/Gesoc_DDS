package domain.excepciones;

public class ExceptionFaltaParametros extends RuntimeException {
    public ExceptionFaltaParametros(String message) {
        super(message);
    }
}
