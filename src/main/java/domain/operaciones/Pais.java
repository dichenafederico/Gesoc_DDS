package domain.operaciones;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Paises")
@Table(name = "Paises")
public class Pais {
    @Enumerated(EnumType.ORDINAL)
    public NombrePais nombre;
    @Column(name = "Pais_Id")
    public String paisId;
    @Column(name = "lenguaje_id")
    public String lenguajeId;
    @Column(name = "divisa_id")
    public String divisaId;
    @Enumerated(EnumType.ORDINAL)
    public NombreDivisa moneda;
    @Column(name = "separador_decimales")
    public String separadorDecimales;
    @Column(name = "separador_miles")
    public String separadorMiles;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pais_id")
    public List<Provincia> provincias = new ArrayList<>();
    @Id
    @GeneratedValue
    private Long id;

    /*No inclui todos los datos del pais , solo los que me parecen relevantes , despues agrego el
     * los demas si hacen falta*/

    public Pais() {
    }

    public Pais(NombrePais nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return this.nombre.getNombre();
    }

    public String getId() {
        return paisId;
    }

    public void setId(String id) {
        this.paisId = id;
    }

    public String getLenguajeId() {
        return lenguajeId;
    }

    public void setLenguajeId(String id) {
        this.lenguajeId = id;
    }

    public String getDivisaId() {
        return divisaId;
    }

    public void setDivisaId(String id) {
        this.divisaId = id;
    }

    public String getSeparadorDecimales() {
        return separadorDecimales;
    }

    public void setSeparadorDecimales(String id) {
        this.separadorDecimales = id;
    }

    public String getSeparadorMiles() {
        return separadorMiles;
    }

    public void setSeparadorMiles(String id) {
        this.separadorMiles = id;
    }

    public void agregarProvincia(Provincia provincia) {
        if (this.provincias.contains(provincia)) {
        } else {
            this.provincias.add(provincia);
        }
    }

    public List<Provincia> getProvincias() {
        return this.provincias;
    }
}
