package domain;

import domain.entidades.CategoriaEntidad;
import domain.entidades.Comando;
import domain.entidades.Entidad;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "Organizaciones")
public class Organizacion {

    @ElementCollection
    @Column(name = "etiqueta")
    private final List<String> etiquetasOrganizacion;
    @OneToMany
    @OrderColumn(name = "posicion")
    public List<Entidad> entidades = new ArrayList<>();
    @ManyToMany
    public Set<CategoriaEntidad> categoriasEntidadesDeOrganizacion = new HashSet<>();
    @Id
    @GeneratedValue
    public Long id;

    public Organizacion() {
        this.etiquetasOrganizacion = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void agregarEtiqueta(String etiqueta) {
        this.etiquetasOrganizacion.add(etiqueta);
    }

    public List<String> getEtiquetasOrganizacion() {
        return etiquetasOrganizacion;
    }

    /*Decidi que la organizacion agrege y quite comandos a cualquier categoria sin importar de que entidad
     * sea y que varias entidades puedan compartir categorias */

    public void agregarComandoAcategoria(String nombreComando, String nombreCategoria) {
        Comando comando = this.buscarComando(nombreComando);
        CategoriaEntidad categoria = this.buscarCategoriaXnombre(nombreCategoria);
        if (categoria != null) {
            categoria.getComandos().add(comando);
        } else {
            System.out.println("La categoria indicada no pertenece a la organizacion");
        }

    }

    public Comando buscarComando(String nombre) {
        List<Comando> comandos = Arrays.asList(Comando.values());

        comandos = comandos.stream().filter(c -> c.getDescripcion().equals(nombre)).collect(Collectors.toList());
        return comandos.get(0);
    }

    public void quitarComandoAcategoria(String nombreComando, String nombreCategoria) {
        Comando comando = this.buscarComando(nombreComando);
        CategoriaEntidad categoria = this.buscarCategoriaXnombre(nombreCategoria);
        if (categoria != null) {
            if (categoria.getComandos().contains(comando)) {
                categoria.getComandos().remove(comando);
            } else {
                System.out.println("La categoria indicada no tenia ese comando");
            }
        } else {
            System.out.println("La categoria indicada no pertenece a la organizacion");
        }
    }

    public CategoriaEntidad buscarCategoriaXnombre(String nombreCategoria) {
        return this.categoriasEntidadesDeOrganizacion.stream().filter(categoria -> categoria.getNombre().equals(nombreCategoria)).collect(Collectors.toList()).get(0);
    }

    public void dejarEntidadSinCategoria(Entidad entidad) {
        if (entidades.contains(entidad)) {
            entidad.setCategoriaEntidad(null);
        } else {
            System.out.println("La entidad indicada no pertenece a la organizacion");
        }
    }

    public void definirCategoriaDeEntidad(Entidad entidad, String nombreCategoria) {
        CategoriaEntidad categoria = this.buscarCategoriaXnombre(nombreCategoria);
        if (categoria != null) {
            if (entidades.contains(entidad) && categoriasEntidadesDeOrganizacion.contains(categoria)) {
                entidad.setCategoriaEntidad(categoria);
            } else {
                System.out.println("La categoria o la entidad no pertenece a la organizacion");
            }
        } else {
            System.out.println("La categoria indicada no pertenece a la organizacion");
        }
    }

    public void crearYagregarCategoria(String nombre) {
        CategoriaEntidad categoria = new CategoriaEntidad(nombre);
        this.agregarCategoria(categoria);
    }

    public void agregarCategoria(CategoriaEntidad categoria) {
        this.categoriasEntidadesDeOrganizacion.add(categoria);
    }

    public void eliminarCategoria(CategoriaEntidad categoria) {
        if (this.categoriasEntidadesDeOrganizacion.contains(categoria)) {
            this.categoriasEntidadesDeOrganizacion.remove(categoria);
        } else {
            System.out.println("La categoria no pertence a la organizacion");
        }
    }

    /* Decidi que pueden agregarse entidades sin tener que darles categoria y categorias
    sin tener que setearles la misma a alguna entidad de la organizacion.Y que cada entidad puede tener una sola
     categoria como maximo*/
    public void ejecutarComandosDeEntidades() {
        this.entidades.forEach(entidad -> this.ejecutarComandosDeUnaEntidad(entidad));
    }

    public void ejecutarComandosDeUnaEntidad(Entidad entidad) {
        if (this.validarEntidad(entidad)) {
            entidad.getCategoriaEntidad().getComandos().forEach(comando -> comando.ejecutar(entidad));
        }
    }

    public void ejecutarComandoEnTodasEntidades(Comando comando) { /*Las que lo tienen*/
        this.entidades.forEach(entidad -> this.ejecutarComandoDeUnaEntidad(entidad, comando));
    }

    public void ejecutarComandoDeUnaEntidad(Entidad entidad, Comando comando) {
        if (this.validarEntidad(entidad)) {
            if (entidad.getCategoriaEntidad().getComandos().contains(comando)) {
                comando.ejecutar(entidad);
            }
        }
    }

    public void ejecutarComandoEnEntidadesDeUnaCategoria(Comando comando, CategoriaEntidad categoriaEntidad) { /*Las que lo tienen*/
        this.getEntidadesDeUnaCategoria(categoriaEntidad).forEach(entidad -> this.ejecutarComandoDeUnaEntidad(entidad, comando));
    }

    public void ejecutarComandosDeEntidadesDeUnaCategoria(Comando comando, CategoriaEntidad categoriaEntidad) {
        this.getEntidadesDeUnaCategoria(categoriaEntidad).forEach(entidad -> this.ejecutarComandosDeUnaEntidad(entidad));
    }

    public boolean validarEntidad(Entidad entidad) { /*Por pertecencia de entidad y de su categoria */
        if (entidades.contains(entidad)) {
            return this.categoriasEntidadesDeOrganizacion.contains(entidad.getCategoriaEntidad());
        } else {
            return false;
        }
    }

    public void agregarEntidad(Entidad entidad) {
        this.entidades.add(entidad);
        if (entidad.getCategoriaEntidad() != null) {
            this.agregarCategoria(entidad.getCategoriaEntidad());
        }

    }

    public List<Entidad> getEntidadesDeUnaCategoria(CategoriaEntidad categoriaEntidad) {
        if (this.categoriasEntidadesDeOrganizacion.contains(categoriaEntidad)) {
            return this.entidades.stream().filter(entidad -> entidad.getCategoriaEntidad() == categoriaEntidad).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    public List<Entidad> getEntidades() {
        return entidades;
    }

    public Set<CategoriaEntidad> getCategorias() {
        return categoriasEntidadesDeOrganizacion;
    }
}
