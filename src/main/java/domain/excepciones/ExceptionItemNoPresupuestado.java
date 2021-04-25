package domain.excepciones;

public class ExceptionItemNoPresupuestado extends RuntimeException {
    public ExceptionItemNoPresupuestado(String message) {
        super(message);
    }
}
