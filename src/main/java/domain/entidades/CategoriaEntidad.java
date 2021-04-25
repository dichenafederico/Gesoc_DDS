package domain.entidades;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Categorias_de_entidades")
@Table(name = "Categorias_de_entidades")
public class CategoriaEntidad {
    public String nombre;
    @ElementCollection
    @Enumerated(EnumType.ORDINAL)
    public Set<Comando> comandos = new HashSet<>();
    @Id
    @GeneratedValue
    public Long id;

    public CategoriaEntidad() {
    }

    public CategoriaEntidad(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void cambiarNombre(String nombreNuevo) {
        this.nombre = nombreNuevo;
    }

    public Set<Comando> getComandos() {
        return comandos;
    }

    public Long getId() {
        return id;
    }
}
