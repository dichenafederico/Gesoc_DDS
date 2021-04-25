package controllers;

import domain.entidades.*;
import domain.excepciones.ExceptionCuitInvalido;
import domain.excepciones.ExceptionDireccionPostalInvalida;
import domain.repositorios.RepositorioCategorias;
import domain.repositorios.RepositorioEntidades;
import domain.usuarios.Usuario;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EntidadesController extends DataSessionController implements WithGlobalEntityManager, TransactionalOps {

    public ModelAndView getEntidades(Request request, Response response) {
        super.setDataSesion(request);
        List<Entidad> entidades = RepositorioEntidades.getInstance().traerEntidades();
        entidades = entidades.stream().filter(e -> e.getOrganizacion() == organizacion).collect(Collectors.toList());
        List<CategoriaEntidad> categorias = RepositorioCategorias.getInstance().traerCategorias();
        modelo.put("entidades", entidades);
        modelo.put("categorias", categorias);
        return new ModelAndView(modelo, "entidades.html.hbs");
    }

    public ModelAndView getEntidadesPorCategoria(Request request, Response response) {
        super.setDataSesion(request);
        List<Entidad> entidades = RepositorioEntidades.getInstance().traerEntidades();
        entidades = entidades.stream().filter(e -> e.getOrganizacion() == organizacion).collect(Collectors.toList());
        String categoria = request.queryParams("nombre");
        if (categoria.equals("Sin Categoria")) {
            entidades = entidades.stream().filter(e -> e.getCategoriaEntidad() == null).collect(Collectors.toList());
            modelo.put("entidades", entidades);
        } else {
            String finalCategoria = categoria;
            entidades = entidades.stream().filter(e -> e.getCategoriaEntidad() != null).collect(Collectors.toList());
            entidades = entidades.stream().filter(e -> finalCategoria.equals(e.getCategoriaEntidad().getNombre())).collect(Collectors.toList());
            modelo.put("entidades", entidades);
        }
        modelo.put("sinEntidades", entidades.size() == 0);
        return new ModelAndView(modelo, "entidadesPorCategoria.html.hbs");
    }

    public ModelAndView nuevaBase(Request request, Response response) {
        super.setDataSesion(request);
        return new ModelAndView(modelo, "nuevaEntidadBase.html.hbs");
    }

    public ModelAndView nuevaJuridica(Request request, Response response) {
        super.setDataSesion(request);
        return new ModelAndView(modelo, "nuevaEntidadJuridica.html.hbs");
    }

    public ModelAndView crearEntidadBase(Request request, Response response) {
        super.setDataSesion(request);
        String nombre = request.queryParams("nombre");
        String descripcion = request.queryParams("descripcion");
        BigDecimal limite = new BigDecimal(request.queryParams("limite"));
        EntidadBase entidadNueva = new EntidadBase(nombre, organizacion, limite);
        entidadNueva.setDescripcion(descripcion);
        withTransaction(() -> RepositorioEntidades.getInstance().agregarEntidad(entidadNueva));
        response.redirect("/entidades");
        return null;
    }

    public ModelAndView crearEntidadJuridica(Request request, Response response) {
        super.setDataSesion(request);
        String nombre = request.queryParams("nombre");
        String direccionPostal = request.queryParams("direccionPostal");
        String cuit = request.queryParams("cuit");
        String razonSocial = request.queryParams("razonSocial");
        String codigoDeInscripcionEnIGC = request.queryParams("codigoDeInscripcionEnIGC");
        BigDecimal limite = new BigDecimal(request.queryParams("limite"));
        EntidadJuridica nuevaEntidad = new EntidadJuridica(nombre, organizacion, limite);
        if (this.ValidarCuit(cuit)) {
            nuevaEntidad.setCuit(Long.parseLong(cuit));
        } else {
            throw new ExceptionCuitInvalido("Cuit invalido");
        }
        nuevaEntidad.setCodigoInscripcionEnIGJ(Long.parseLong(codigoDeInscripcionEnIGC));
        nuevaEntidad.setRazonSocial(razonSocial);
        if (direccionPostal.length() == 4) {
            nuevaEntidad.setDireccionPostal(Integer.parseInt(direccionPostal));
        } else {
            throw new ExceptionDireccionPostalInvalida("La direccion debe ser de 4 digitos");
        }
        withTransaction(() -> RepositorioEntidades.getInstance().agregarEntidad(nuevaEntidad));
        response.redirect("/entidades");
        return null;
    }

    public boolean ValidarCuit(String cuit) {
        char[] digitos = cuit.toCharArray();
        int[] factores = new int[]{5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
        int total = 0;
        int indice = 0;
        while (indice != 10) {
            total += Integer.parseInt(String.valueOf(digitos[indice])) * factores[indice];
            indice++;
        }
        /*Ahi suma el producto de cada digito del cuit , menos el ultimo , con su factor correspondiente */
        int control = 11 - (total % 11); /*Ahi calcula el valor de control que debe ser igual al digito verificador
del cuit , que es el ultimo*/
        return control - Integer.parseInt(String.valueOf(digitos[10])) == 0;/*comparo con resta porque es clase el derecho
no numero*/
    }

    public List<Comando> getComandosTODOS() {
        return new ArrayList<Comando>(Arrays.asList(Comando.values()));
    }

    public ModelAndView nuevaCategoria(Request request, Response response) {
        super.setDataSesion(request);
        Set<CategoriaEntidad> categorias = organizacion.getCategorias();
        modelo.put("categorias", categorias);
        List<Comando> comandos = this.getComandosTODOS();
        /*new ArrayList<Comando>(Arrays.asList(Comando.values()));*/
        modelo.put("descripciones", comandos);
        return new ModelAndView(modelo, "nuevaCategoria.html.hbs");
    }

    public ModelAndView crearCategoria(Request request, Response response) {
        super.setDataSesion(request);
        String comando = request.queryParams("comando");
        String nombre = request.queryParams("nombre");
        organizacion.crearYagregarCategoria(nombre);
        organizacion.agregarComandoAcategoria(comando, nombre);
        CategoriaEntidad nuevaCategoria = organizacion.buscarCategoriaXnombre(nombre);
        withTransaction(() -> RepositorioCategorias.getInstance().agregarCategoria(nuevaCategoria));
        response.redirect("/nuevaCategoria");
        return null;
    }

    public CategoriaEntidad obtenerCategoriaPorNombre(String nombre) {
        List<CategoriaEntidad> categorias = RepositorioCategorias.getInstance().traerCategorias();
        categorias = categorias.stream().filter(c -> c.getNombre().equals(nombre)).collect(Collectors.toList());
        return categorias.get(0);
    }

    public Entidad obtenerEntidadPorNombre(String nombre) {
        List<Entidad> entidades = RepositorioEntidades.getInstance().traerEntidades();
        entidades = entidades.stream().filter(c -> c.getNombreFicticio().equals(nombre)).collect(Collectors.toList());
        return entidades.get(0);
    }


    public ModelAndView detalleCategoria(Request request, Response response) {
        super.setDataSesion(request);
        String nombre = request.params(":nombre");
        CategoriaEntidad categoria = this.obtenerCategoriaPorNombre(nombre);
        List<Comando> comandos = categoria.getComandos().stream().collect(Collectors.toList());
        modelo.put("categoria", categoria);
        modelo.put("comandos", comandos);
        return new ModelAndView(modelo, "detalleCategoria.html.hbs");
    }

    public ModelAndView detalleEntidad(Request request, Response response) {
        super.setDataSesion(request);
        String nombre = request.params(":nombre");
        Entidad entidad = this.obtenerEntidadPorNombre(nombre);
        List<Usuario> usuarios = entidad.getUsuarios();
        modelo.put("entidades", entidad);
        modelo.put("usuarios", entidad);
        if (entidad.getTipo().equals("Entidad base")) {
            return new ModelAndView(modelo, "detalleEntidadBase.html.hbs");
        } else {
            EntidadJuridica laEntidad = (EntidadJuridica) entidad;
            List<EntidadBase> entidadesBase = laEntidad.getEntidadesBase();
            modelo.put("entidadesBase", entidadesBase);
            return new ModelAndView(modelo, "detalleEntidadJuridica.html.hbs");
        }
    }

    public ModelAndView administrarCategoria(Request request, Response response) {
        super.setDataSesion(request);
        Set<CategoriaEntidad> categorias = organizacion.getCategorias();
        modelo.put("categorias", categorias);
        List<Comando> comandos = this.getComandosTODOS();
        modelo.put("descripciones", comandos);
        return new ModelAndView(modelo, "editarCategoria.html.hbs");
    }

    public ModelAndView administrarEntidad(Request request, Response response) {
        super.setDataSesion(request);
        Set<CategoriaEntidad> categorias = organizacion.getCategorias();
        modelo.put("categorias", categorias);
        List<Entidad> entidades = organizacion.getEntidades();
        modelo.put("entidades", entidades);
        return new ModelAndView(modelo, "editarEntidad.html.hbs");
    }

    public ModelAndView editarCategoria(Request request, Response response) {
        super.setDataSesion(request);
        String categoria = request.queryParams("categoria");
        String comando = request.queryParams("comando");
        String accion = request.queryParams("accion");
        CategoriaEntidad laCategoria = this.obtenerCategoriaPorNombre(categoria);
        if (accion.equals("Agregar")) {
            organizacion.agregarComandoAcategoria(comando, categoria);
        }
        if (accion.equals("Quitar")) {
            organizacion.quitarComandoAcategoria(comando, categoria);
        }
        RepositorioCategorias.getInstance().update(laCategoria);
        response.redirect("/categorias/" + categoria);
        return null;
    }

    public ModelAndView asociarEntidad(Request request, Response response) {
        super.setDataSesion(request);
        String categoria = request.queryParams("categoria");
        String entidad = request.queryParams("entidad");
        Entidad laEntidad = this.obtenerEntidadPorNombre(entidad);
        CategoriaEntidad laCategoria = this.obtenerCategoriaPorNombre(categoria);
        organizacion.definirCategoriaDeEntidad(laEntidad, categoria);
        RepositorioEntidades.getInstance().update(laEntidad);
        response.redirect("/entidades/" + entidad);
        return null;
    }

    public ModelAndView desasociarEntidad(Request request, Response response) {
        super.setDataSesion(request);
        String entidad = request.queryParams("entidad");
        Entidad laEntidad = this.obtenerEntidadPorNombre(entidad);
        organizacion.dejarEntidadSinCategoria(laEntidad);
        RepositorioEntidades.getInstance().update(laEntidad);
        response.redirect("/entidades/" + entidad);
        return null;
    }
}
