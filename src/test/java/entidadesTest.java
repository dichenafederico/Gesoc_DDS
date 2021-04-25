import domain.APIs.MLMock;
import domain.Organizacion;
import domain.entidades.Comando;
import domain.entidades.EntidadBase;
import domain.entidades.EntidadJuridica;
import domain.excepciones.ExceptionEntidadBloqueadaParaComprar;
import domain.operaciones.*;
import domain.usuarios.Estandar;
import domain.usuarios.Usuario;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class entidadesTest {


    public EntidadBase entidadBase;
    public Organizacion organizacion;
    public EntidadJuridica entidadJuridica;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    /*Esto es para generar egresos*/
    Usuario usuarioVerificador;
    DocumentoComercial doc;
    Proveedor provedor;
    MLMock api;
    MedioDePago medio;
    Egreso egreso1;
    Egreso egreso2;
    private String nombre;


    /*aca se inicializan las variables definidas arriba , lo cual se repite para cada test ,
     para que cada uno sea independiente del resto*/
    @Before
    public void setUp() {
        organizacion = new Organizacion();
        entidadBase = new EntidadBase("entidadB", organizacion, new BigDecimal(100));
        entidadJuridica = new EntidadJuridica("entidadJ", organizacion, new BigDecimal(100));
        organizacion.crearYagregarCategoria("categoria3"); /*Porque tiene los 3 comandos existentes*/
        organizacion.agregarComandoAcategoria(Comando.BLOQUEAR_ENTIDAD_BASE_PARA_JURIDICA.getDescripcion(), "categoria3");
        organizacion.agregarComandoAcategoria(Comando.NO_ACEPTAR_MAS_EGRESOS.getDescripcion(), "categoria3");
        organizacion.agregarComandoAcategoria(Comando.NO_AGREGAR_MAS_ENTIDADES_BASE.getDescripcion(), "categoria3");
        organizacion.definirCategoriaDeEntidad(entidadJuridica, "categoria3");
        organizacion.definirCategoriaDeEntidad(entidadBase, "categoria3");

        /*Lo que sigue es para generar egresos */

        api = new MLMock();
        medio = new MedioDePago("Visa", TipoDePago.TARJETA_CREDITO);

        doc = new DocumentoComercial();
        provedor = new Proveedor(api, NombrePais.ARGENTINA, "1407", "Sarachaga", 5416, 1, "a", 204013551, "Proveedor", "Prueba");
        String contraseniaValida = "Contraseni@Valida123";
        usuarioVerificador = new Usuario("Carlos", contraseniaValida, new Estandar());

    }

    /*En las metodos que se testean decidi que no se dispare una excepcion por cada error trivial de usuario (tratar
    de eliminar un comando de una categoria , cuando este no estaba ella , por ejemplo) porque no se justifica que el
   programa se cierre cada vez que se comete un erro asi , dado que el programa puede seguir corriendo normalmente*/

    @Test
    public void funcionaAgregarEntidadesBase() {
        entidadJuridica.agregarEntidadBase(entidadBase);
        assertEquals("entidadB", entidadJuridica.getEntidadesBase().get(0).getNombreFicticio());
    }

    @Test
    public void funcionaElComandoQueBloqueaEntidadesJuridicas() {
        organizacion.ejecutarComandosDeUnaEntidad(entidadJuridica);
        entidadJuridica.agregarEntidadBase(entidadBase);
        assertTrue(entidadJuridica.getEntidadesBase().isEmpty());
    }

    /*Decidi que el comando no quite a la entidad base de las entidades juridicas a las que fue agregada antes de
     * ser bloqueada porque el dominio no lo pedia y creo que no es lo deseable */
    @Test
    public void funcionaElComandoQueBloqueaEntidadesBase() {
        organizacion.ejecutarComandosDeUnaEntidad(entidadBase);
        entidadJuridica.agregarEntidadBase(entidadBase);
        assertTrue(entidadJuridica.getEntidadesBase().isEmpty());
    }

    @Test
    public void funcionaElComandoQueBloqueaEgresos() {
        exceptionRule.expect(ExceptionEntidadBloqueadaParaComprar.class);
        exceptionRule.expectMessage("La entidad esta bloqueada para hacer el egreso");

        List<Item> items = new ArrayList<>();
        Item item = new Item("Monitor 4k");
        item.setValor(new BigDecimal(35));
        Item item2 = new Item("Television led");
        item2.setValor(new BigDecimal(20));
        Item item3 = new Item("PC de escritorio gama alta");
        item3.setValor(new BigDecimal(55));
        items.add(item);
        items.add(item2);
        items.add(item3);

        egreso1 = new Egreso(doc, medio, provedor, entidadBase, items, null, false, usuarioVerificador);

        organizacion.ejecutarComandosDeUnaEntidad(entidadBase);

        List<Item> items2 = new ArrayList<>();
        Item item4 = new Item("Auriculares inalambricos");
        item4.setValor(new BigDecimal(15));
        Item item5 = new Item("Joystick de PC con cable");
        item5.setValor(new BigDecimal(5));
        items2.add(item4);
        items2.add(item5);
        egreso2 = new Egreso(doc, medio, provedor, entidadBase, items2, null, false, usuarioVerificador);
    }

}