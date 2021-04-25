package domain.APIs;

import domain.operaciones.Divisa;
import domain.operaciones.Pais;
import domain.operaciones.Provincia;
import domain.operaciones.Ubicacion;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ML_mock")
public class MLMock implements Api {
    @Id
    @GeneratedValue
    private Long id;

    public void setInfoDivisa(String pais, Divisa divisa) {
    }

    public void setUbicacionConApi(Ubicacion ubicacion, Pais pais) {
        if (ubicacion.getCodigoPostal() == "1407" && ubicacion.getPais().getNombre() == "Argentina") {
            ubicacion.getCiudad().setNombre("Tu ciudad es tu provincia(esta es una ciudad)");
            ubicacion.getProvincia().setNombre("Capital Federal");
        }
    }

    public String consultarProvincia(Ubicacion ubicacion, Pais pais) {
        return "no usar este metodo";
    }

    public String consultarCiudad(Ubicacion ubicacion, Provincia provincia) {
        return "no usar este metodo";
    }
}
