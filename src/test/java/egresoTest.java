import Utils.EgresoBuilder;
import domain.Organizacion;
import domain.Reportes.ReporteGastosMensualesEntidad;
import domain.entidades.EntidadBase;
import domain.excepciones.ExceptionPresupuestoSinEgreso;
import domain.operaciones.Egreso;
import domain.operaciones.Item;
import domain.operaciones.Presupuesto;
import domain.repositorios.RepositorioEgresos;
import domain.usuarios.Estandar;
import domain.usuarios.Usuario;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import security.ValidacionesEgresos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class egresoTest implements WithGlobalEntityManager, TransactionalOps {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    Usuario usuarioVerificador;
    Usuario usuarioVerificador2;
    EntidadBase entidadPruebaReporte;
    EgresoBuilder builder;
    Calendar cal = Calendar.getInstance();
    List<Item> items = new ArrayList<>();
    List<Item> items2 = new ArrayList<>();

    /*aca se inicializan las variables definidas arriba , lo cual se repite para cada test , para que cada uno sea
    independiente del resto*/
    @Before
    public void setUp() {

        RepositorioEgresos repoEgresos = RepositorioEgresos.getInstance();

        Item item = new Item("Television 4k");
        item.setValor(new BigDecimal(10));
        Item item2 = new Item("Television led");
        item2.setValor(new BigDecimal(15));
        Item item3 = new Item("Television 4k");
        item3.setValor(new BigDecimal(65));
        items.add(item);
        items.add(item2);
        items2.add(item3);
        String contraseniaValida = "Contraseni@Valida123";
        usuarioVerificador = new Usuario("Carlos", contraseniaValida, new Estandar());
        usuarioVerificador2 = new Usuario("Juan", contraseniaValida, new Estandar());

        Organizacion organizacion = new Organizacion();
        entidadPruebaReporte = new EntidadBase("Entidad prueba", organizacion, (new BigDecimal(1000)));

        builder = new EgresoBuilder();
        builder.agregarItems(items);
        builder.setOrganizacion(organizacion);
        builder.setUsuarioVerificador(usuarioVerificador);
        builder.setEntidad(entidadPruebaReporte);
        builder.setEtiqueta("Inmuebles");
        builder.build();
        Egreso egreso = builder.getEgreso();

        withTransaction(() -> {
            RepositorioEgresos.getInstance().agregarEgreso(egreso);
        });

        builder.agregarPresupuesto(items);
        egreso.realizarValidaciones();

        builder.reset();
        builder.agregarItems(items2);
        builder.setOrganizacion(new Organizacion());
        builder.setUsuarioVerificador(usuarioVerificador2);
        builder.setEntidad(entidadPruebaReporte);
        builder.setEtiqueta("Inmuebles");
        builder.build();
        Egreso egreso2 = builder.getEgreso();

        withTransaction(() -> {
            RepositorioEgresos.getInstance().agregarEgreso(egreso2);
        });
        builder.agregarPresupuesto(items2);
        builder.agregarPresupuesto(items);
        builder.seleccionarPresupuestoFinal();
        egreso2.realizarValidaciones();

        builder.reset();
        builder.agregarItems(items2);
        builder.setOrganizacion(new Organizacion());
        builder.setUsuarioVerificador(usuarioVerificador2);
        builder.setEntidad(entidadPruebaReporte);
        builder.setEtiqueta("Inmuebles");
        builder.build();
        Egreso egreso3 = builder.getEgreso();

        withTransaction(() -> {
            RepositorioEgresos.getInstance().agregarEgreso(egreso3);
        });
        builder.agregarPresupuesto(items);
        builder.seleccionarPresupuestoFinal();
        egreso3.realizarValidaciones();
    }

    @Test
    public void testPresupuestoSinEgreso() {
        exceptionRule.expect(ExceptionPresupuestoSinEgreso.class);
        exceptionRule.expectMessage("El ingreso del egreso es obligatorio");
        new Presupuesto(builder.getEgreso().getDocumentoAsociado(), new ArrayList<>(), null);
    }

    @Test
    public void testNoRebotarPorProveedorSeleccionado() {
        assertTrue(!usuarioVerificador.existeMensajeValidacion(ValidacionesEgresos.VALIDACION_PROVEEDOR_CORRECTO.getMensajeValidacion()));
    }

    @Test
    public void testRebotarCantidadDePresupuestos() {
        assertTrue(usuarioVerificador.existeMensajeValidacion(ValidacionesEgresos.VALIDACION_CANTIDAD_PRESUPUESTOS.getMensajeValidacion()));
    }

    @Test
    public void testNoRebotarPorItemNoPresupuestado() {
        assertTrue(!usuarioVerificador.existeMensajeValidacion(ValidacionesEgresos.VALIDACION_ITEM_EXISTENTE.getMensajeValidacion()));
    }

    @Test
    public void testRebotarPorItemNoPresupuestado() {
        assertTrue(usuarioVerificador2.existeMensajeValidacion(ValidacionesEgresos.VALIDACION_ITEM_EXISTENTE.getMensajeValidacion()));
    }

    @Test
    public void testRebotarPorProveedorSeleccionado() {
        assertTrue(usuarioVerificador2.existeMensajeValidacion(ValidacionesEgresos.VALIDACION_PROVEEDOR_CORRECTO.getMensajeValidacion()));
    }

    /*@Test
    public void testNoRebotarCantidadDePresupuestos() {
        assertTrue(!usuarioVerificador.existeMensajeValidacion(ValidacionesEgresos.VALIDACION_CANTIDAD_PRESUPUESTOS.getMensajeValidacion()));
    }*/

    @Test
    public void testReporteGastosEntidad() {
        ReporteGastosMensualesEntidad reportePrueba = entidadPruebaReporte.generarReporteMensual(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
        assertEquals(new BigDecimal(465), reportePrueba.gastoPorEtiqueta("Inmuebles"));
    }

}