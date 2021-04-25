package domain.operaciones;

import domain.APIs.Api;

import javax.persistence.*;

@Entity(name = "Divisas")
@Table(name = "Divisas")
public class Divisa {
    @Column(name = "divisa_id")
    public String divisaId;
    public String descripcion;
    public String simbolo;
    @Enumerated(EnumType.ORDINAL)
    public NombreDivisa tipo;
    @Column(name = "cantidad_decimales")
    public int cantidadDecimales;
    @Id
    @GeneratedValue
    private Long id;

    public Divisa() {
    }

    public Divisa(NombreDivisa tipo, Api api) {
        this.tipo = tipo;
        this.descripcion = tipo.getNombre();

        this.setInfo(api);
    }

    public void setInfo(Api api) {
        api.setInfoDivisa(this.descripcion, this);
    }

    public String getNombre() {
        return tipo.getNombre();
    }

    public NombreDivisa getTipo() {
        return tipo;
    }

    public String getId() {
        return divisaId;
    }

    public void setId(String id) {
        divisaId = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public int getDecimales() {
        return cantidadDecimales;
    }

    public void setDecimales(int numero) {
        cantidadDecimales = numero;
    }

}
