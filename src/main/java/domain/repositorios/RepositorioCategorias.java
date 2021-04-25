package domain.repositorios;

import domain.entidades.CategoriaEntidad;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.List;

public class RepositorioCategorias implements WithGlobalEntityManager {


    private static final RepositorioCategorias instance = new RepositorioCategorias();


    private RepositorioCategorias() {
    }

    public static RepositorioCategorias getInstance() {
        return instance;
    }

    public void agregarCategoria(CategoriaEntidad nuevaCategoria) {
        entityManager().persist(nuevaCategoria);
    }

    public List<CategoriaEntidad> traerCategorias() {
        return entityManager().createQuery("from Categorias_de_entidades").getResultList();
    }

    public void update(CategoriaEntidad categoria) {
        entityManager().remove(categoria);
        entityManager().persist(categoria);
    }

}
