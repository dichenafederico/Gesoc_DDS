package domain.repositorios;

import domain.operaciones.MedioDePago;
import domain.operaciones.Proveedor;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.List;

public class RepositorioUtil implements WithGlobalEntityManager {

    private static final RepositorioUtil INSTANCE = new RepositorioUtil();

    public static RepositorioUtil instance() {
        return INSTANCE;
    }

    public List<MedioDePago> traerMediosDePago() {
        return entityManager()
                .createQuery("FROM MedioDePago")
                .getResultList();
    }

    public List<Proveedor> traerProveedores() {
        return entityManager()
                .createQuery("FROM Proveedor")
                .getResultList();
    }

    public Proveedor findProveedorById(Long id) {
        return entityManager().find(Proveedor.class, id);
    }

    public MedioDePago findMedioDePagoById(Long id) {
        return entityManager().find(MedioDePago.class, id);
    }

    //public List<Proveedor>

    //TODO: busqueda de proveedores, mediosDePago, etc para llenar combos en UI

}

