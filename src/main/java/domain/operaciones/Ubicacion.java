package domain.operaciones;

import domain.APIs.Api;
import domain.repositorios.RepositorioLugares;

import javax.persistence.*;

@Embeddable
@Table(name = "Ubicaciones")
public class Ubicacion {
    @ManyToOne(cascade = CascadeType.ALL)
    public Pais pais;
    @ManyToOne(cascade = CascadeType.ALL)
    public Provincia provincia;
    @ManyToOne(cascade = CascadeType.ALL)
    public Ciudad ciudad;
    @Column(name = "Codigo_postal")
    public String codigoPostal;
    public String calle;
    public int altura;
    public int piso; /*Si es una casa , corresponde 0*/
    public String departamento; /*Asi sea letra o numero se trata de string.
     Si es una casa , corresponde A o 0 (el caracter '0') */

    public Ubicacion() {
    }

    public Ubicacion(String codigo_postal, String calle, int altura, int piso, String departamento, NombrePais pais, Api api) {
        this.codigoPostal = codigo_postal;
        this.calle = calle;
        this.altura = altura;
        this.piso = piso;
        this.departamento = departamento;
        RepositorioLugares.getInstance().obtenerLugares(api, pais, this);
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }
}
