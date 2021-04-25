package domain.repositorios;

import domain.APIs.Api;
import domain.APIs.MercadoLibre;
import domain.operaciones.Divisa;
import domain.operaciones.NombreDivisa;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import java.util.List;
import java.util.stream.Collectors;

public class RepositorioDivisas implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {
    private static final RepositorioDivisas instance = new RepositorioDivisas();

    public Api api = new MercadoLibre();

    private RepositorioDivisas() {
    }

    public static RepositorioDivisas getInstance() {
        return instance;
    }

    public void agregarDivisa(Divisa nuevaDivisa) {
        entityManager().persist(nuevaDivisa);
    }

    public List<Divisa> buscarDivisa(NombreDivisa tipo) {
        List<Divisa> divisas = this.traerDivisas();
        return divisas.stream().filter(divisa -> divisa.getNombre().equals(tipo.getNombre())).collect(Collectors.toList());
    }

    public List<Divisa> traerDivisas() {
        return entityManager().createQuery("from Divisas").getResultList();
    }

    public Divisa getDivisa(NombreDivisa tipo) {
        List<Divisa> divisaBuscada = this.buscarDivisa(tipo);
        if (divisaBuscada.isEmpty()) {
            Divisa nuevaDivisa = new Divisa(tipo, api);
            withTransaction(() -> {
                this.agregarDivisa(nuevaDivisa);
            });
            return nuevaDivisa;

        } else {
            return divisaBuscada.get(0);
        }
    }
}
