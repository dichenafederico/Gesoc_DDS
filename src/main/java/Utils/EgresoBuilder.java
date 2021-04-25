package Utils;

import domain.APIs.MLMock;
import domain.Organizacion;
import domain.entidades.Entidad;
import domain.entidades.EntidadBase;
import domain.operaciones.*;
import domain.repositorios.RepositorioEgresos;
import domain.usuarios.Estandar;
import domain.usuarios.Usuario;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EgresoBuilder implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

    public List<Item> items = new ArrayList<>();
    public String etiqueta = "";
    Egreso egreso;
    MLMock api = new MLMock();
    DocumentoComercial doc = new DocumentoComercial();
    MedioDePago medio = new MedioDePago("Visa",TipoDePago.TARJETA_CREDITO);
    Proveedor provedor = new Proveedor(api, NombrePais.ARGENTINA, "1407", "Sarachaga", 5416, 1, "a", 204013551, "Proveedor", "Prueba");
    Organizacion organizacion = new Organizacion();
    String contraseniaValida = "Contraseni@Valida123";
    List<Presupuesto> presupuestos = new ArrayList<>();
    Entidad entidad = new EntidadBase("Entidad prueba", organizacion, (new BigDecimal(1000)));
    Usuario usuarioVerificador = new Usuario("Usuario prueba", contraseniaValida, new Estandar());

    public EgresoBuilder() {
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    public void setOrganizacion(Organizacion organizacion) {
        this.organizacion = organizacion;
    }

    public void setProvedor(Proveedor proveedor) {
        this.provedor = proveedor;
    }

    public void setMedio(MedioDePago medioDePago) {
        this.medio = medioDePago;
    }

    public void setUsuarioVerificador(Usuario usuario) {
        this.usuarioVerificador = usuario;
    }

    public void agregarItems(List<Item> items) {
        this.items = items;
    }

    public void agregarItem(Item item) {
        this.items.add(item);
    }

    public void agregarPresupuesto(List<Item> items) {
        egreso.agregarPresupuesto(new Presupuesto(egreso.getDocumentoAsociado(), items, egreso));
        //RepositorioEgresos.getInstance().update(egreso);
    }

    public void setEtiqueta(String nombreEtiqueta) {
        this.organizacion.agregarEtiqueta(nombreEtiqueta);
        this.etiqueta = nombreEtiqueta;
    }

    public void seleccionarPresupuestoFinal() {
        egreso.seleccionarPresupuesto(egreso.getPresupuestos().get(0));
    }

    public void build() {
        egreso = new Egreso(doc, medio, provedor, entidad, items, presupuestos, false, usuarioVerificador);
        Calendar cal = Calendar.getInstance();
        egreso.setFecha(cal.getTime());
        egreso.setEtiquetaReporte(this.etiqueta);
    }

    public Egreso getEgreso() {
        return this.egreso;
    }

    public void reset() {
        this.egreso = null;
        this.presupuestos = new ArrayList<>();
    }
}
