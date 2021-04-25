package security;

import domain.excepciones.ExceptionContraseniaNoSegura;
import domain.excepciones.ExceptionFileNotFound;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Validaciones {

    ESCORTA {
        public void realizarValidacion(String contrasenia) {
            if (contrasenia.length() < CONTRASENIACORTA) {
                throw new ExceptionContraseniaNoSegura("Contrasenia Corta");
            }
        }
    },
    ESCOMUN {
        public void realizarValidacion(String contrasenia) {
            Stream<String> content;
            try (InputStream inputStream = getClass().getResourceAsStream(FILE);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                content =  reader.lines();
                if (content.anyMatch(c -> c.equals(contrasenia))) {
                    throw new ExceptionContraseniaNoSegura("Contrasenia Comun");
                }
            } catch (IOException | UncheckedIOException e) {  //| URISyntaxException e
                throw new ExceptionFileNotFound("No se encontro el archivo");
            }
        }
    },
    NOCONTIENENUMERONIMAYUSCULA {
        public void realizarValidacion(String contrasenia) {
            final Pattern pattern = Pattern.compile(CONTIENEMAYUSCULAYNUMERO, Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(contrasenia);
            if (!matcher.matches()) {
                throw new ExceptionContraseniaNoSegura("Contrasenia Sin Numeros Ni Mayusculas");
            }
        }
    },
    NOCONTIENECARACTERESESPECIALES {
        public void realizarValidacion(String contrasenia) {
            final Pattern pattern = Pattern.compile(CONTIENECARACTERESESPECIALES, Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(contrasenia);
            if (!matcher.matches()) {
                throw new ExceptionContraseniaNoSegura("Contrasenia Sin Caracteres Especiales");
            }
        }
    };

    //Se pueden externalizar si en algun momento es necesario
    private static final int CONTRASENIACORTA = 8;
    private static final String CONTIENEMAYUSCULAYNUMERO = "^(?:(?=.*\\d)(?=.*[A-Z]).*)[^\\s]$";
    private static final String CONTIENECARACTERESESPECIALES = "^(?:(?=.*[@#$%^&+=]).*)$";
    private static final String FILE = "/10k-most-common.txt";
    public abstract void realizarValidacion(String contrasenia);

}
