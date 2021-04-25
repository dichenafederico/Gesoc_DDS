package domain.APIs;

import domain.operaciones.Divisa;
import domain.operaciones.Pais;
import domain.operaciones.Provincia;
import domain.operaciones.Ubicacion;

public interface Api {

    void setInfoDivisa(String pais, Divisa divisa);

    void setUbicacionConApi(Ubicacion ubicacion, Pais pais);

    String consultarProvincia(Ubicacion ubicacion, Pais pais);

    String consultarCiudad(Ubicacion ubicacion, Provincia provincia);

}
