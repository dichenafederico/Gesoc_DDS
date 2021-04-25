package domain.repositorios;

import domain.APIs.Api;
import domain.operaciones.*;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.List;
import java.util.stream.Collectors;

public class RepositorioLugares implements WithGlobalEntityManager {

    /* Decidi que las provincias y ciudades no esten directamente en una coleccion porque se mezclarian las
     * de paises distintos , creo que es lo mismo que el repositorio los tenga a travez de los paises*/

    private static final RepositorioLugares instance = new RepositorioLugares();

    private RepositorioLugares() {
    }

    public static RepositorioLugares getInstance() {
        return instance;
    }

    public List<Pais> buscarPais(NombrePais np) {
        List<Pais> paises = this.traerPaises();
        return paises.stream().filter(pais -> pais.getNombre().equals(np.getNombre())).collect(Collectors.toList());
    }

    public List<Provincia> buscarProvincia(Pais pais, String provinciaName) {
        return pais.getProvincias().stream().filter(provincia -> provincia.getNombre().equals(provinciaName)).collect(Collectors.toList());
    }

    public List<Ciudad> buscarCiudad(Provincia provincia, String ciudadName) {
        return provincia.getCiudades().stream().filter(ciudad -> ciudad.getNombre().equals(ciudadName)).collect(Collectors.toList());
    }

    public List<Pais> traerPaises() {
        return entityManager().createQuery("from Paises").getResultList();
    }

    /*public List<Provincia> traerProvincias() {
        return this.getEntityManager().createQuery("from Provincias").getResultList();
    }
    public List<Ciudad> traerCiudades() {
        return this.getEntityManager().createQuery("from Ciudades").getResultList();
    } Estos dos metodos solo fueron para verificar algunas cosas en testing */
    public void agregarPais(Pais nuevoPais) {
        entityManager().persist(nuevoPais);
    }

    public void agregarCiudad(Ciudad nuevaCiudad) {
        entityManager().persist(nuevaCiudad);
    }

    public void agregarProvincia(Provincia nuevaProvincia) {
        entityManager().persist(nuevaProvincia);
    }

    public void obtenerLugares(Api api, NombrePais np, Ubicacion ubicacion) {
        List<Pais> paisBuscado = this.buscarPais(np);
        /* Si el pais estaba en el repo , la lista tiene solo a ese pais y , sino , queda una lista vacia*/
        if (paisBuscado.isEmpty()) {
            this.cargarConApi(api, np, ubicacion);
        } else {
            this.cargarConRepo(api, ubicacion, paisBuscado.get(0));
        }
    }

    public void cargarConApi(Api api, NombrePais np, Ubicacion ubicacion) {
        Pais pais = new Pais(np);
        Provincia provincia = new Provincia();
        Ciudad ciudad = new Ciudad();
        ubicacion.setPais(pais);
        ubicacion.setProvincia(provincia);
        ubicacion.setCiudad(ciudad);
        api.setUbicacionConApi(ubicacion, pais);
    }

    public void cargarConRepo(Api api, Ubicacion ubicacion, Pais pais) {
        ubicacion.setPais(pais);
        String provinciaName = api.consultarProvincia(ubicacion, pais);
        List<Provincia> pTemp = this.buscarProvincia(pais, provinciaName);
        if (!pTemp.isEmpty()) {
            Provincia p = pTemp.get(0);
            ubicacion.setProvincia(p);
            String ciudadName = api.consultarCiudad(ubicacion, p);
            List<Ciudad> cTemp = this.buscarCiudad(p, ciudadName);
            if (!cTemp.isEmpty()) {
                ubicacion.setCiudad(cTemp.get(0));
            }
        }
    }
}/*Puse esos dos ifs  para que no rompan los test de egresos y de entidades. Me paso algo rarisimo y es que
si los saco y corre todos los test , esos rompen.Pero si corro solo esos test por separado NO ROMPEN
Por lo que , es un problema con la base de datos , algo que no queda en el estado inicial*/
