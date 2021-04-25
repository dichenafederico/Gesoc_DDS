import controllers.EntidadesController;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EntidadesControllerTest {
    EntidadesController driver = new EntidadesController();

    /*aca se inicializan las variables definidas arriba , lo cual se repite para cada test ,
     para que cada uno sea independiente del resto*/
    @Test
    public void validarCuitValido() {
        assertTrue(driver.ValidarCuit("20370989142")); /*Es valido xq es el mio*/
    }

    @Test
    public void rebotarCuitInvalido() {
        assertFalse(driver.ValidarCuit("20370989149")); /*El anterior con otro digito al final , es invalido porque
         el ultimo digito es de verificacion de los anteriores*/
    }
}
