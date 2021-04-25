package domain.excepciones;

public class ExceptionContraseniaNoSegura extends RuntimeException {
    public ExceptionContraseniaNoSegura(String message) {
        super(message);
    }
}
