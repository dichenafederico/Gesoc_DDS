package controllers;

import Utils.EgresoBuilder;
import domain.excepciones.ExceptionFaltaParametros;
import domain.operaciones.Egreso;
import domain.operaciones.Item;
import domain.operaciones.MedioDePago;
import domain.operaciones.Proveedor;
import domain.repositorios.RepositorioEgresos;
import domain.repositorios.RepositorioUsuarios;
import domain.repositorios.RepositorioUtil;
import domain.usuarios.Usuario;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.util.List;

public class EgresosController extends DataSessionController implements WithGlobalEntityManager, TransactionalOps {

    public ModelAndView crearEgreso(Request request, Response response) {

        BigDecimal valorItem = new BigDecimal(request.queryParams("valorItem"));

        super.setDataSesion(request);

        String idProveedor = request.queryParams("proveedor");
        String idMedioDePago = request.queryParams("medioPago");
        String descripcionItem = request.queryParams("descripcionItem");
        if (idProveedor.isEmpty() || idMedioDePago.isEmpty() || descripcionItem.isEmpty())
            throw new ExceptionFaltaParametros("Faltan cargar datos obligatorios");

        Proveedor proveedor = RepositorioUtil.instance().findProveedorById(Long.parseLong(idProveedor));
        MedioDePago medioPago = RepositorioUtil.instance().findMedioDePagoById(Long.parseLong(idMedioDePago));
        Item item = new Item(descripcionItem);
        item.setValor(valorItem);

        Usuario usuarioVerificador = RepositorioUsuarios.instance().findUsuarioById(Long.parseLong(request.queryParams("usuarioVerificador")));

        String etiqueta = request.queryParams("etiqueta");

        EgresoBuilder builder = new EgresoBuilder();
        builder.setOrganizacion(organizacion);
        builder.setEntidad(entidad);
        builder.setEtiqueta(etiqueta);
        builder.setProvedor(proveedor);
        builder.setUsuarioVerificador(usuarioVerificador);
        builder.agregarItem(item);
        builder.build();

        Egreso egreso = builder.getEgreso();

        withTransaction(() -> {
            RepositorioEgresos.getInstance().agregarEgreso(egreso);
        });

        response.redirect("/egresos");
        return null;
    }

    public ModelAndView nuevo(Request req, Response res) {
        super.setDataSesion(req);
        traerDatosIniciales();
        return new ModelAndView(modelo, "nuevoEgreso.html.hbs");
    }

    public void traerDatosIniciales() {
        modelo.put("proveedores", RepositorioUtil.instance().traerProveedores());
        modelo.put("mediosDePago", RepositorioUtil.instance().traerMediosDePago());
        modelo.put("usuarios", RepositorioUsuarios.instance().traerUsuarios());
        modelo.put("etiquetas", organizacion.getEtiquetasOrganizacion());
    }

    public ModelAndView getEgresos(Request request, Response response) {
        super.setDataSesion(request);
        List<Egreso> egresos = RepositorioEgresos.getInstance().getEgresos();
        modelo.put("egresos", egresos);
        return new ModelAndView(modelo, "egresos.html.hbs");
    }


}
