import Utils.CrearEgresos;
import domain.Reportes.ReporteGastosMensualesEntidad;
import domain.repositorios.RepositorioEgresos;

public class ValidarEgresosJob {
    public static void main(String[] args) {
        System.out.println("INICIO BATCH");
        RepositorioEgresos repo = RepositorioEgresos.getInstance();
        System.out.println("Cantidad de Egresos NO Validados: " + repo.getEgresos().stream().filter(egreso1 -> !egreso1.egresoVerificado).count());
        System.out.println("Cantidad de Egresos Validados: " + repo.getEgresos().stream().filter(egreso1 -> egreso1.egresoVerificado).count());
        System.out.println("VALIDANDO EGRESOS");
        RepositorioEgresos.getInstance().validarEgresos();
        System.out.println("Cantidad de Egresos NO Validados: " + repo.getEgresos().stream().filter(egreso1 -> !egreso1.egresoVerificado).count());
        System.out.println("Cantidad de Egresos Validados: " + repo.getEgresos().stream().filter(egreso1 -> egreso1.egresoVerificado).count());

    }
}
