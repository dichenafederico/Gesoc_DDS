package domain.entidades;

import domain.Organizacion;
import domain.Reportes.ReporteGastosMensualesEntidad;
import domain.excepciones.ExceptionEntidadBloqueadaParaComprar;
import domain.operaciones.Egreso;
import domain.usuarios.Usuario;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Entidades")
public abstract class Entidad {

    public BigDecimal montoLimite = new BigDecimal(0);
    public BigDecimal montoAlcanzado = new BigDecimal(0);
    public boolean bloqueada = false;
    public boolean bloqueadaParaComprar = false;
    public String nombreFicticio;
    @ManyToOne
    public CategoriaEntidad categoriaEntidad;
    @OneToMany
    @JoinColumn(name = "entidad_id")
    public List<Usuario> usuarios = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.ALL)
    public Organizacion organizacion;
    @OneToMany
    public List<Egreso> egresos = new ArrayList<>();
    @Id
    @GeneratedValue
    public Long id;

    public Long getId() {
        return id;
    }

    public void agregarEgreso(Egreso egreso) {
        if (bloqueadaParaComprar) {
            throw new ExceptionEntidadBloqueadaParaComprar("La entidad esta bloqueada para hacer el egreso");
        } else {
            this.egresos.add(egreso);
        }
    }

    public void setMontoAlcanzado() {
        this.montoAlcanzado = new BigDecimal(0);
        int tope = this.egresos.size();
        int indice = 0;
        while (indice != tope) {
            this.montoAlcanzado = this.montoAlcanzado.add(this.egresos.get(indice).getMontoTotal());
            indice = indice + 1;
        }
    }

    public BigDecimal getMontoAlcanzado() {
        this.setMontoAlcanzado();
        return montoAlcanzado;
    }

    public List<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public CategoriaEntidad getCategoriaEntidad() {
        return categoriaEntidad;
    }


    /*Decidi que si la organizacion conserva las categorias que haya creado incluso aunque
     * las elimine de todas las entidades que haya marcado con ellas*/
    public void setCategoriaEntidad(CategoriaEntidad categoriaEntidad) {
        if (categoriaEntidad == null) {
            this.categoriaEntidad = null;
        }
        this.categoriaEntidad = categoriaEntidad;
        this.organizacion.agregarCategoria(categoriaEntidad);
    }

    public boolean bloqueadaOno() {
        return bloqueada;
    }

    public BigDecimal getMontoLimite() {
        return montoLimite;
    }

    public void bloquearOdesbloquear(Boolean valor) {
        this.bloqueada = valor;
    }

    public void bloquearOdesbloquearParaComprar(Boolean valor) {
        this.bloqueadaParaComprar = valor;
    }

    public BigDecimal gastosEgresos(List<Egreso> egresos) {
        return egresos.stream().map(egreso -> egreso.getMontoTotal()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getNombreFicticio() {
        return nombreFicticio;
    }

    public Organizacion getOrganizacion() {
        return organizacion;
    }

    public List<String> etiquetasOrganizacion() {
        return this.getOrganizacion().getEtiquetasOrganizacion();
    }

    public ReporteGastosMensualesEntidad generarReporteMensual(int anio, int mes) {
        ReporteGastosMensualesEntidad reporte = new ReporteGastosMensualesEntidad(anio, mes, this);
        reporte.crearReporte();
        return reporte;
    }

    public void agregarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }

    public abstract String getTipo();

    public String getBloqueadaParaComprar() {
        if (this.bloqueadaParaComprar) {
            return "si";
        } else {
            return "no";
        }
    }

    public String getBloqueada() {
        if (this.bloqueada) {
            return "si";
        } else {
            return "no";
        }
    }
}
