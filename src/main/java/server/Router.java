package server;

import controllers.EgresosController;
import controllers.EntidadesController;
import controllers.HomeController;
import controllers.UsuariosController;
import server.spark.utils.BooleanHelper;
import server.spark.utils.HandlebarsTemplateEngineBuilder;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class Router {

    private static void chequearLoginInexistente(Request req, Response res) {
        String usuario = req.session().attribute("user");
        if (usuario == null) {
            res.redirect("/login");
            Spark.halt();
        }
    }

    private static void chequearLoginExistente(Request req, Response res) {
        String usuario = req.session().attribute("user");
        if (usuario != null) {
            res.redirect("/");
            Spark.halt();
        }
    }

    public static void configure() {
        System.out.println("Iniciando servidor");
        Spark.staticFileLocation("/public");

        HandlebarsTemplateEngine engine = HandlebarsTemplateEngineBuilder
                .create()
                .withDefaultHelpers()
                .withHelper("isTrue", BooleanHelper.isTrue)
                .build();

        //Spark.staticFiles.location("/public");

        HomeController homeController = new HomeController();
        EgresosController egresosController = new EgresosController();
        EntidadesController entidadesController = new EntidadesController();
        UsuariosController usuariosController = new UsuariosController();

        Spark.get("/", (request, response) -> homeController.getHome(request, response), engine);
        Spark.get("/login", UsuariosController::show, engine);
        Spark.post("/login", UsuariosController::login, engine);
        Spark.post("/logout", usuariosController::logout, engine);
        Spark.get("/egreso", (request, response) -> egresosController.nuevo(request, response), engine);
        Spark.post("/egreso", egresosController::crearEgreso, engine);
        Spark.get("/egresos", egresosController::getEgresos, engine);
        Spark.get("/entidades", entidadesController::getEntidades, engine);
        Spark.get("/entidades/DeCategoria", entidadesController::getEntidadesPorCategoria, engine);
        Spark.get("/nuevaCategoria", entidadesController::nuevaCategoria, engine);
        Spark.post("/nuevaCategoria", entidadesController::crearCategoria, engine);
        Spark.get("/categorias/:nombre", entidadesController::detalleCategoria, engine);
        Spark.get("/administrarCategoria", entidadesController::administrarCategoria, engine);
        Spark.get("/categoriaEditada", entidadesController::editarCategoria, engine);
        Spark.get("/entidadBase", entidadesController::nuevaBase, engine);
        Spark.post("/entidadBase", entidadesController::crearEntidadBase, engine);
        Spark.get("/entidadJuridica", entidadesController::nuevaJuridica, engine);
        Spark.post("/entidadJuridica", entidadesController::crearEntidadJuridica, engine);
        Spark.get("/entidades/:nombre", entidadesController::detalleEntidad, engine);
        Spark.get("/administrarEntidad", entidadesController::administrarEntidad, engine);
        Spark.post("/entidadEditada", entidadesController::asociarEntidad, engine);
        Spark.get("/entidadEditada", entidadesController::desasociarEntidad, engine);
        Spark.get("/bandejaMensajes", usuariosController::getBandejaMensajes, engine);

        Spark.before("/entidadJuridica", Router::chequearLoginInexistente);
        Spark.before("/entidadBase", Router::chequearLoginInexistente);
        Spark.before("/administrarEntidad", Router::chequearLoginInexistente);
        Spark.before("/administrarCategoria", Router::chequearLoginInexistente);
        Spark.before("/egresos/*", Router::chequearLoginInexistente);
        Spark.before("/egreso", Router::chequearLoginInexistente);
        Spark.before("/entidades/*", Router::chequearLoginInexistente);
        Spark.before("/entidades", Router::chequearLoginInexistente);
        Spark.before("/bandejaMensajes", Router::chequearLoginInexistente);
        Spark.before("/categorias", Router::chequearLoginInexistente);
        Spark.before("/categorias/*", Router::chequearLoginInexistente);
        Spark.before("/nuevaCategoria", Router::chequearLoginInexistente);
        Spark.before("/categoriaEditada", Router::chequearLoginInexistente);

        Spark.before("/login", Router::chequearLoginExistente);

    }
}