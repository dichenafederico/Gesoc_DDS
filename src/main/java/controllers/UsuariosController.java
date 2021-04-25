package controllers;

import domain.repositorios.RepositorioEgresos;
import domain.repositorios.RepositorioUsuarios;
import domain.usuarios.Mensaje;
import domain.usuarios.Usuario;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import security.Criptografia;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;

public class UsuariosController extends DataSessionController implements WithGlobalEntityManager, TransactionalOps {

    private static final HashMap<String, Object> badLoginResponsePayload = new HashMap<String, Object>() {
        private static final long serialVersionUID = 1L;

        {
            put("feedback", "Usuario o contraseña invalidas");
        }
    };

    public static ModelAndView show(Request req, Response res) {
        return new ModelAndView(null, "inicioSesion.html.hbs");
    }

    public static ModelAndView login(Request req, Response res) {
        String nombreUsuario = req.queryParams("usuario");
        String password = req.queryParams("contrasenia");

        List<Usuario> usuarios = RepositorioUsuarios.instance().traerUsuarios();
        Usuario usuario = RepositorioUsuarios.instance().obtenerUsuario(usuarios, nombreUsuario);

        if (usuario == null) {
            return new ModelAndView(badLoginResponsePayload, "inicioSesion.html.hbs");
        }

        System.out.println(usuario);

        String contraseniaHasheada = Criptografia.hashearContrasenia(password, usuario.getSalt());

        System.out.println(contraseniaHasheada);

        if (usuario.getContrasenia().equals(contraseniaHasheada)) {
            System.out.println("Login exitoso");
            req.session(true);
            req.session().attribute("user", nombreUsuario);
            res.redirect("/");
        } else {
            return new ModelAndView(badLoginResponsePayload, "inicioSesion.html.hbs");
        }

        return null;
    }

    public ModelAndView logout(Request req, Response res) {
        req.session().invalidate();
        res.redirect("/");
        return null;
    }

    public ModelAndView getBandejaMensajes(Request request, Response response) {

        RepositorioEgresos.getInstance().validarEgresos();

        super.setDataSesion(request);

        List<Mensaje> mensajes = usuario.getBandejaMensajes();

        modelo.put("mensajes", mensajes);

        modelo.put("sinMensajes", mensajes.size() == 0);

        return new ModelAndView(modelo, "bandejaMensajes.html.hbs");
    }
}

