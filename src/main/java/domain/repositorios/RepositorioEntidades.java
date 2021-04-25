package domain.repositorios;

import domain.entidades.Entidad;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.List;
import java.util.stream.Collectors;

public class RepositorioEntidades implements WithGlobalEntityManager {

    private static final RepositorioEntidades instance = new RepositorioEntidades();

    private RepositorioEntidades() {
    }

    public static RepositorioEntidades getInstance() {
        return instance;
    }

    public void agregarEntidad(Entidad nuevaEntidad) {
        entityManager().persist(nuevaEntidad);
    }

    public Entidad buscar(long Id) {
        return entityManager().find(Entidad.class, Id);
    }

    public List<Entidad> traerEntidades() {
        return entityManager().createQuery("from Entidad", Entidad.class).getResultList();
    }

    public List<Entidad> getEntidadesPorCategoria(String nombreCategoria) {
        List<Entidad> entidades = this.traerEntidades();
        return entidades.stream()
                .filter(e -> e.getCategoriaEntidad().getNombre().equals(nombreCategoria))
                .collect(Collectors.toList());
    }

    public void update(Entidad entidad) {
        entityManager().remove(entidad);
        entityManager().persist(entidad);
    }

}
