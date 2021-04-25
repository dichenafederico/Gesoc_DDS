import domain.excepciones.ExceptionContraseniaNoSegura;
import domain.usuarios.Estandar;
import domain.usuarios.TipoUsuario;
import domain.usuarios.Usuario;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import security.Criptografia;

import static org.junit.Assert.assertEquals;

/*Aca van las definiciones de las variables que se usen luego , para no repetir los datos a poner
 * si van a tener el mismo valor y de paso que tengan un nombre que digan que son*/
public class validacionContraseniaTest {
    private final TipoUsuario tipo = new Estandar();
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    private String nombre;

    /*aca se inicializan las variables definidas arriba , lo cual se repite para cada test , para que cada uno sea
    independiente del resto*/
    @Before
    public void setUp() {
        nombre = "alex";
    }

    @Ignore
    @Test
    public void testRebotarContraseniaComun() {
        exceptionRule.expect(ExceptionContraseniaNoSegura.class);
        exceptionRule.expectMessage("Contrasenia Comun");
        new Usuario(nombre, "mercedes", tipo);
    }

    @Test
    public void testRebotarContraseniaCorta() {
        exceptionRule.expect(ExceptionContraseniaNoSegura.class);
        exceptionRule.expectMessage("Contrasenia Corta");
        new Usuario(nombre, "corta", tipo);
    }

    @Test
    public void testRebotarContraseniaSinNumerosNiMayusculas() {
        exceptionRule.expect(ExceptionContraseniaNoSegura.class);
        exceptionRule.expectMessage("Contrasenia Sin Numeros Ni Mayusculas");
        new Usuario(nombre, "contraseniasinnumeronimayuscul@", tipo);
    }

    @Test
    public void testRebotarContraseniaSinCaracteresEspeciales() {
        exceptionRule.expect(ExceptionContraseniaNoSegura.class);
        exceptionRule.expectMessage("Contrasenia Sin Caracteres Especiales");
        new Usuario(nombre, "contraseniasinCaracteresEspeciales10", tipo);
    }

    @Test
    public void testNoRebotarContraseniaValida() {
        String contrasenia = "Contraseni@Valida123";
        Usuario usuario = new Usuario(nombre, contrasenia, tipo);
        assertEquals(usuario.getContrasenia(), Criptografia.hashearContrasenia(contrasenia, usuario.getSalt()));
    }
}