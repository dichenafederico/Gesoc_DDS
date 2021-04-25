package domain.operaciones;

import domain.entidades.Entidad;
import domain.excepciones.ExceptionFaltaParametros;
import domain.usuarios.Usuario;
import security.ValidacionesEgresos;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "Egresos")
public class Egreso {

    @OneToOne(cascade = CascadeType.ALL)
    public DocumentoComercial documentoAsociadoEgreso;
    @ManyToOne(cascade = CascadeType.ALL)
    public MedioDePago medioDePago;
    public Date fecha;
    @ManyToOne(cascade = CascadeType.ALL)
    public Entidad entidad;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "egreso_id")
    public List<Item> items = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "egreso_id")
    public List<Presupuesto> presupuestos = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    public Presupuesto presupuestoSeleccionado;
    public Boolean egresoVerificado = false;
    public Boolean egresoConfirmado = false; //TODO: consultar (cuando realmente el egreso es un gasto confirmado?)
    public Boolean requierePresupuestos;
    @ManyToOne(cascade = CascadeType.ALL)
    public Usuario usuarioVerificador;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "criterio_seleccion_proveedor")
    public CriteriosSeleccionProveedor criterio;
    public String etiquetaReporte;
    @Embedded
    private Dinero montoTotal;
    @ManyToOne(cascade = CascadeType.ALL)
    private Proveedor proveedor;
    @Id
    @GeneratedValue
    private Long id;

    public Egreso() {
    }

    public Egreso(DocumentoComercial documentoAsociado, MedioDePago medioDePago, Proveedor proveedor, Entidad entidad, List<Item> items, List<Presupuesto> presupuestos, Boolean requierePresupuestos, Usuario usuarioVerificador) {
        if (documentoAsociado == null || medioDePago == null || proveedor == null || entidad == null || items == null)
            throw new ExceptionFaltaParametros("Faltan datos para completar un egreso válido");
        setDocumentoAsociado(documentoAsociado);
        setMedioDePago(medioDePago);
        setProveedor(proveedor);
        setEntidad(entidad);
        setItems(items);
        setPresupuestos(presupuestos);
        this.requierePresupuestos = requierePresupuestos;
        this.usuarioVerificador = usuarioVerificador;
        this.criterio = CriteriosSeleccionProveedor.MENORVALOR;
        this.calcularMontoTotal();
        this.fecha = new Date();
        entidad.agregarEgreso(this);
        //TODO: Ver seteo de fecha, al momento de crearlo aca o al momento de confirmarse?
    }

    public Usuario getUsuarioVerificador() {
        return usuarioVerificador;
    }

    public void setUsuarioVerificador(Usuario usuarioVerificador) {
        this.usuarioVerificador = usuarioVerificador;
    }

    public DocumentoComercial getDocumentoAsociado() {
        return documentoAsociadoEgreso;
    }

    public void setDocumentoAsociado(DocumentoComercial documentoAsociado) {
        this.documentoAsociadoEgreso = documentoAsociado;
    }

    public Presupuesto getPresupuestoSeleccionado() {
        return presupuestoSeleccionado;
    }

    public Long getId() {
        return id;
    }

    public Boolean getEgresoConfirmado() {
        return egresoConfirmado;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal.getCantidad();
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal.setCantidad(montoTotal);
    }

    public MedioDePago getMedioDePago() {
        return medioDePago;
    }

    public void setMedioDePago(MedioDePago medioDePago) {
        this.medioDePago = medioDePago;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    public List<Presupuesto> getPresupuestos() {
        return presupuestos;
    }

    public void setPresupuestos(List<Presupuesto> presupuestos) {
        this.presupuestos = presupuestos;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void agregarItem(Item item) {
        this.items.add(item);
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getCantidadPresupuestos() {
        return this.presupuestos.size();
    }

    public void agregarPresupuesto(Presupuesto presupuesto) {
        this.presupuestos.add(presupuesto);
    }

    public void seleccionarPresupuesto(Presupuesto presupuesto) {
        this.presupuestoSeleccionado = presupuesto;
    }

    public void calcularMontoTotal() {
        this.montoTotal = new Dinero(new BigDecimal(0), NombreDivisa.PESO_ARGENTINO);
        this.items.forEach(item -> setMontoTotal(getMontoTotal().add(item.getValor())));
    }

    public boolean existeItemEnPresupuestos(Item itemEgreso) {
        return this.getPresupuestos().size() > 0 && this.presupuestos.stream().anyMatch(presupuesto -> presupuesto.existeItem(itemEgreso));
    }

    public BigDecimal presupuestoMinimo() {
        return this.presupuestos.stream().map(presupuesto -> presupuesto.getMontoTotal()).min(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
    }

    public Dinero getMontoTotalPresupuesto() {
        return this.presupuestoSeleccionado.montoTotal;
    }

    public void realizarValidaciones() {
        Arrays.asList(ValidacionesEgresos.values())
                .forEach(validacion -> verificarEgreso(validacion));
    }


    public void agregarMensajeAUsuarioVerificador(String nuevoMensaje) {
        this.getUsuarioVerificador().agregarMensajeALaBandeja(nuevoMensaje, this);
    }

    public void setEtiquetaReporte(String etiqueta) {
        this.etiquetaReporte = etiqueta;
    }

    public boolean contieneEtiqueta(String nombreEtiqueta) {
        return (this.etiquetaReporte.equalsIgnoreCase(nombreEtiqueta));
    }

    public boolean perteneceAlMes(int anio, int mes) {
        //TODO: ver si hay mejor manera que esta
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(this.fecha);
        return cal.get(Calendar.YEAR) == anio && cal.get(Calendar.MONTH) == mes;
    }

    public void verificarEgreso(ValidacionesEgresos validacion) {
        if (validacion.esValido(this))
            agregarMensajeAUsuarioVerificador(validacion.getMensajeValidacion());
        egresoVerificado = true;
    }
}
