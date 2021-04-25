package domain.excepciones;

public class ExceptionFileNotFound extends RuntimeException {
    public ExceptionFileNotFound(String message) {
        super(message);
    }
}