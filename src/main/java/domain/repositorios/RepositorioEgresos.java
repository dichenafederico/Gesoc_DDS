package domain.repositorios;

import domain.entidades.Entidad;
import domain.operaciones.Egreso;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.List;
import java.util.stream.Collectors;

public class RepositorioEgresos implements WithGlobalEntityManager {

    private static final RepositorioEgresos instance = new RepositorioEgresos();

    private RepositorioEgresos() {
    }

    public static RepositorioEgresos getInstance() {
        return instance;
    }

    public void agregarEgreso(Egreso nuevoEgreso) {
        entityManager().persist(nuevoEgreso);
    }

    public Egreso update(Egreso egreso) {
        entityManager().merge(egreso);
        return egreso;
    }

    public void validarEgresos() {
        List<Egreso> egresos = this.getEgresos();
        egresos.stream().filter(egreso -> !egreso.egresoVerificado).forEach(egreso -> egreso.realizarValidaciones());
    }

    @SuppressWarnings("unchecked")
    public List<Egreso> getEgresos() {
        return entityManager()
                .createQuery("from Egreso", Egreso.class)
                .getResultList();
    }

    public Egreso getEgreso(Long id) {
        return entityManager().find(Egreso.class, id);
    }

    public List<Egreso> getEgresosEntidad(Entidad entidad) {
        return entityManager()
                .createQuery("from Egreso e where e.entidad = entidad")
                .getResultList();
    }

    public List<Egreso> obtenerEgresosConfirmadosPorMesDeLaEnitdad(int anio, int mes, Entidad entidad) {
        return getEgresos().stream().filter(egreso ->
                egreso.getEntidad().getNombreFicticio() == entidad.getNombreFicticio() &&
                        egreso.perteneceAlMes(anio, mes) //&&  egreso.getEgresoConfirmado()
        ).collect(Collectors.toList());
    }

    public List<Egreso> obtenerEgresosPorEtiqueta(List<Egreso> egresos, String nombreEtiqueta) {
        return getEgresos().stream().filter(egreso -> egreso.contieneEtiqueta(nombreEtiqueta)).collect(Collectors.toList());
    }

}
