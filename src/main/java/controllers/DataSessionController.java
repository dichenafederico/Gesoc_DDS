package controllers;

import domain.Organizacion;
import domain.entidades.Entidad;
import domain.repositorios.RepositorioUsuarios;
import domain.usuarios.Usuario;
import spark.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DataSessionController {

    public Map<String, Object> modelo = new HashMap<>();

    public String usuarioSesion = null;
    Usuario usuario;
    Entidad entidad;
    Organizacion organizacion;

    public Map<String, Object> getModelo() {
        return modelo;
    }

    public void setDataSesion(Request request) {
        usuarioSesion = request.session().attribute("user");
        Boolean estaLogeado = usuarioSesion != null && !usuarioSesion.isEmpty();
        modelo.put("usuarioLogeado", estaLogeado);
        if (estaLogeado) {
            List<Usuario> usuarios = RepositorioUsuarios.instance().traerUsuarios();
            usuario = RepositorioUsuarios.instance().obtenerUsuario(usuarios, request.session().attribute("user"));
            entidad = usuario.entidad;
            organizacion = entidad.organizacion;
        }
    }


}
