package domain.repositorios;

import domain.usuarios.Mensaje;
import domain.usuarios.Usuario;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.List;
import java.util.stream.Collectors;

public class RepositorioUsuarios implements WithGlobalEntityManager {

    private static final RepositorioUsuarios INSTANCE = new RepositorioUsuarios();

    public static RepositorioUsuarios instance() {
        return INSTANCE;
    }

    public List<Usuario> traerUsuarios() {
        return entityManager()
                .createQuery("FROM Usuario", Usuario.class)
                .getResultList();
    }

    public Usuario update(Usuario usuario) {
        entityManager().merge(usuario);
        return usuario;
    }

    public Usuario obtenerUsuario(List<Usuario> usuarios, String nombreUsuario) {
        List<Usuario> usuario = usuarios.stream().filter(user -> user.getNombre().equalsIgnoreCase(nombreUsuario)).collect(Collectors.toList());
        return usuario.size() == 0 ? null : usuario.get(0);
    }

    public Usuario findUsuarioById(Long id) {
        return entityManager().find(Usuario.class, id);
    }

    public void agregarMensaje(Mensaje mensaje) {
        entityManager().persist(mensaje);
    }


}
