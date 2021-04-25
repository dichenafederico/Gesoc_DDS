package controllers;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class HomeController extends DataSessionController {

    public ModelAndView getHome(Request request, Response response) {
        super.setDataSesion(request);
        return new ModelAndView(modelo, "index.html.hbs");
    }
}