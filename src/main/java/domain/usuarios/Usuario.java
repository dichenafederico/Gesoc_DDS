package domain.usuarios;

import domain.entidades.Entidad;
import domain.operaciones.Egreso;
import domain.repositorios.RepositorioUsuarios;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import security.Criptografia;
import security.Validaciones;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "Usuarios")
public class Usuario implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    public List<Mensaje> bandejaMensajes = new ArrayList<>();

    @ManyToOne
    public Entidad entidad;
    @Id
    @GeneratedValue
    private Long id;
    private String nombre;
    @Transient
    private TipoUsuario tipo;
    private String contrasenia;
    private String salt;

    @OneToMany
    @JoinColumn(name = "usuarioAlta_id")
    private List<Egreso> egresos = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(String usuario, String contrasenia, TipoUsuario tipo) {
        setContrasenia(contrasenia);
        setNombre(usuario);
        setTipo(tipo);
        this.bandejaMensajes = new ArrayList<>();
    }

    public void cambiarContrasenia(String contraseniaNueva) {
    }


    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
        entidad.agregarUsuario(this);
    }

    public Long getId() {
        return id;
    }

    public void registrarEgreso(Egreso egreso) {
        egresos.add(egreso);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.realizarValidaciones(contrasenia);
        this.salt = Criptografia.generateSalt();
        this.contrasenia = Criptografia.hashearContrasenia(contrasenia, getSalt());
    }

    public String getSalt() {
        return salt;
    }

    public List<Mensaje> getBandejaMensajes() {
        return bandejaMensajes;
    }

    public void realizarValidaciones(String contrasenia) {
        Arrays.asList(Validaciones.values())
                .forEach(validacion -> validacion.realizarValidacion(contrasenia));
    }

    public List<Egreso> getEgresos() {
        return egresos;
    }

    public void setEgresos(List<Egreso> egresos) {
        this.egresos = egresos;
    }

    public void agregarMensajeALaBandeja(String mensaje, Egreso egreso) {
        Mensaje nuevoMensaje = new Mensaje(mensaje, egreso);
        this.bandejaMensajes.add(nuevoMensaje);
        withTransaction(() -> {
            RepositorioUsuarios.instance().agregarMensaje(nuevoMensaje);
            RepositorioUsuarios.instance().update(this);
        });
    }

    public void leerMensajesBandeja() {
        this.bandejaMensajes = new ArrayList<>();
    }

    public boolean existeMensajeValidacion(String mensajeValidacion) {
        return this.bandejaMensajes.stream().anyMatch(mensaje -> mensaje.getDescripcion() == mensajeValidacion);
    }

}
