package domain.APIs;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import domain.excepciones.ExceptionInvalidRequest;
import domain.operaciones.*;
import domain.repositorios.RepositorioLugares;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.ws.rs.core.MediaType;

@Entity
@Table(name = "Mercado_libre")
public class MercadoLibre implements Api, WithGlobalEntityManager, EntityManagerOps, TransactionalOps {
    @Id
    @GeneratedValue
    private Long id;

    public String pedirAlgo(String recurso) {
        ClientResponse respuesta;
        Client cliente = Client.create();
        respuesta = cliente.resource("https://api.mercadolibre.com/").path(recurso)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        this.validarRespuesta(respuesta);
        return respuesta.getEntity(String.class);
    }

    public void validarRespuesta(ClientResponse respuesta) {
        if (respuesta.getStatus() != 200) { /*200 es el codigo de estado "OK" */
            throw new ExceptionInvalidRequest("La Api no responde o datos de entrada invalidos");
        }
    }

    public String getIdPais(String pais) {
        String datos = this.pedirAlgo("classified_locations/countries");
        int posicion = datos.indexOf(pais);
        return datos.substring(posicion - 12, posicion - 10);
    }

    /*
     Los campos en el string de respuesta de la api estan en distinto orden que la respuesta que
     que figura en la pagina , se v een impresiones , el campo symbol esta luedo de id y antes de descripcion
     */
    public void setInfoDivisa(String descripcion, Divisa divisa) {
        String datos = this.pedirAlgo("currencies");
        setDivisaSimbolo(descripcion, divisa, datos);
        setDineroDecimales(descripcion, divisa, datos);
        setDivisaId(descripcion, divisa, datos);
    }

    public void setDivisaId(String descripcion, Divisa divisa, String datos) {
        datos = extraerHasta(datos, descripcion);
        datos = datos.substring(datos.length() - 40, datos.length() - 25); /*El 25 y 40 aseguran quedar entre*/
        datos = recortarCampo(datos);                                      /* el inicio y fin del campo */
        divisa.setId(datos);
    }

    public void setDivisaSimbolo(String descripcion, Divisa divisa, String texto) {
        texto = extraerHasta(texto, descripcion);
        texto = texto.substring(texto.length() - 25, texto.length() - 15); /*El 25 y 15 aseguran quedar entre*/
        divisa.setSimbolo(recortarCampo(texto)); /*el inicio y final del campo */
    }

    public void setDineroDecimales(String descripcion, Divisa divisa, String texto) {
        String datos = extraerDesde(texto, descripcion);
        datos = extraerDesde(datos, "decimal_places");
        datos = extraerDesde(datos, ":");
        datos = extraerHasta(datos, "}");
        datos = datos.substring(1);
        divisa.setDecimales(Integer.parseInt(datos.trim()));
    }

    public String extraerHasta(String texto, String cadena) {
        return texto.substring(0, texto.indexOf(cadena));
    }

    public String extraerDesde(String texto, String cadena) {
        return texto.substring(texto.indexOf(cadena));
    }

    public String recortarCampo(String cadena) {
        String temp = extraerDesde(cadena, ":");
        temp = temp.substring(2);
        return temp.substring(0, temp.indexOf("\""));
    }

    public String pedirUbicacion(String idPais, String codigoPostal) {
        return this.pedirAlgo("countries/" + idPais + "/zip_codes/" + codigoPostal);
    }

    public String pedirProvinciasDePais(String idPais) {
        return this.pedirAlgo("classified_locations/countries/" + idPais);
    }

    public String pedirCiudadesDeProvincia(String idProvincia) {
        return this.pedirAlgo("classified_locations/states/" + idProvincia);
    }

    public String consultarProvincia(Ubicacion ubicacion, Pais pais) {
        String data = this.pedirUbicacion(pais.getId(), ubicacion.getCodigoPostal());
        data = extraerDesde(data, "state\":");
        data = extraerDesde(data, "name\":");
        return (recortarCampo(data));
    }

    public String consultarCiudad(Ubicacion ubicacion, Provincia provincia) {
        String data = this.pedirUbicacion(ubicacion.getPais().getId(), ubicacion.getCodigoPostal());
        data = this.darDatosCiudad(data);
        if (data.startsWith("\"")) {
            data = extraerDesde(data, "name\":");
            return data.substring(6, data.indexOf("\""));
        } else {
            return data;
        }
    }

    public void setUbicacionConApi(Ubicacion ubicacion, Pais pais) {
        pais.setId(this.getIdPais(pais.getNombre()));
        String datos = this.pedirUbicacion(pais.getId(), ubicacion.getCodigoPostal());
        this.setInfoCiudad(ubicacion.getCiudad(), datos);
        this.setInfoProvincia(ubicacion.getProvincia(), datos);
        this.setInfoPais(pais);
    }

    public void setInfoPais(Pais pais) {
        String datosPais = this.pedirAlgo("classified_locations/countries/" + pais.getId());
        datosPais = extraerDesde(datosPais, "locale\":");
        pais.setLenguajeId(recortarCampo(datosPais));
        datosPais = extraerDesde(datosPais, "currency_id\":");
        pais.setDivisaId(recortarCampo(datosPais));
        datosPais = extraerDesde(datosPais, "decimal_separator\":");
        pais.setSeparadorDecimales(recortarCampo(datosPais));
        datosPais = extraerDesde(datosPais, "thousands_separator\":");
        pais.setSeparadorMiles(recortarCampo(datosPais));
        this.setProvinciasPais(pais);
        withTransaction(() -> {
            RepositorioLugares.getInstance().agregarPais(pais);
        });
    }

    public void setProvinciasPais(Pais pais) {
         /* en esta parte agregar todas las provincias al pais y agregar validacion de eso
        al test correspondiente*/
        Provincia referenciaTemporal;
        String provinciasPais = this.pedirProvinciasDePais(pais.getId());
        provinciasPais = extraerDesde(provinciasPais, "[");
        while (provinciasPais.contains(",")) {
            provinciasPais = extraerDesde(provinciasPais, "id\"");
            referenciaTemporal = new Provincia();
            referenciaTemporal.setId(recortarCampo(provinciasPais));
            provinciasPais = extraerDesde(provinciasPais, "name\"");
            referenciaTemporal.setNombre(recortarCampo(provinciasPais));
            pais.agregarProvincia(referenciaTemporal);
        }
        pais.getProvincias().forEach(pr -> this.setCiudadesDeProvincia(pr));
        pais.getProvincias().forEach(pr -> withTransaction(() -> {
            RepositorioLugares.getInstance().agregarProvincia(pr);
        }));
    }


    public void setInfoProvincia(Provincia provincia, String datos) {
        datos = extraerDesde(datos, "state\":");
        datos = extraerDesde(datos, "id\":");
        provincia.setId(recortarCampo(datos));
        datos = extraerDesde(datos, "name\":");
        provincia.setNombre(recortarCampo(datos));
        this.setCiudadesDeProvincia(provincia);
    }

    public void setCiudadesDeProvincia(Provincia provincia) {
         /* en esta parte agregar todas las ciudades a la provincia y agregar validacion de eso
        al test correspondiente*/
        Ciudad referenciaTemporal;
        String ciudadesProvincia = this.pedirCiudadesDeProvincia(provincia.getId());
        ciudadesProvincia = extraerDesde(ciudadesProvincia, "[");
        while (ciudadesProvincia.contains(",")) {
            ciudadesProvincia = extraerDesde(ciudadesProvincia, "id\"");
            referenciaTemporal = new Ciudad();
            referenciaTemporal.setId(recortarCampo(ciudadesProvincia));
            ciudadesProvincia = extraerDesde(ciudadesProvincia, "name\"");
            referenciaTemporal.setNombre(recortarCampo(ciudadesProvincia));
            Ciudad finalReferenciaTemporal = referenciaTemporal;
            withTransaction(() -> {
                RepositorioLugares.getInstance().agregarCiudad(finalReferenciaTemporal);
            });
            provincia.agregarCiudad(referenciaTemporal);
        }

    }

    public String darDatosCiudad(String texto) {
        texto = extraerDesde(texto, "city\":");
        texto = extraerDesde(texto, "id\":");
        return texto.substring(4, texto.indexOf(","));
    }

    public void setInfoCiudad(Ciudad ciudad, String datos) {
        String temp = this.darDatosCiudad(datos);
        if (temp.startsWith("\"")) {
            ciudad.setId(temp.substring(0, temp.indexOf("\"")));
            datos = extraerDesde(datos, "name\":");
            ciudad.setNombre(datos.substring(6, datos.indexOf("\"")));
        } else {
            ciudad.setId(temp);
            ciudad.setNombre(temp);
        }
    }
}
