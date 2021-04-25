package domain.Reportes;

import domain.entidades.Entidad;
import domain.operaciones.Egreso;
import domain.repositorios.RepositorioEgresos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReporteGastosMensualesEntidad {

    public List<Egreso> egresosDelMes;
    //TODO: cambiar por lista de tupla String, Bigdecimal?
    public List<EtiquetaGastos> etiquetasGastos = new ArrayList<>();
    public Entidad entidad;
    public int anio;
    public int mes;

    public ReporteGastosMensualesEntidad(int anio, int mes, Entidad entidad) {
        this.entidad = entidad;
        this.anio = anio;
        this.mes = mes;
    }

    public void crearReporte() {
        this.egresosDelMes = RepositorioEgresos.getInstance().obtenerEgresosConfirmadosPorMesDeLaEnitdad(anio, mes, entidad);
        entidad.etiquetasOrganizacion().forEach(etiqueta ->
                {
                    EtiquetaGastos etiquetaReporte = new EtiquetaGastos(etiqueta);
                    etiquetaReporte.setGastoMensual(this.gastosEgresos(RepositorioEgresos.getInstance().obtenerEgresosPorEtiqueta(this.egresosDelMes, etiqueta)));
                    etiquetasGastos.add(etiquetaReporte);
                }
        );
    }

    public BigDecimal gastosEgresos(List<Egreso> egresos) {
        return egresos.stream().map(egreso -> egreso.getMontoTotal()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal gastoPorEtiqueta(String nombreEtiqueta) {
        return this.etiquetasGastos.stream().filter(etiqueta -> etiqueta.getNombreEtiqueta().equalsIgnoreCase(nombreEtiqueta)).findAny().orElse(null).getGastoMensual();
    }

    public BigDecimal gastoTotalDelMes() {
        return this.gastosEgresos(this.egresosDelMes);
    }

}
