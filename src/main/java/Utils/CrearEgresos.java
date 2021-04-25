package Utils;

import domain.APIs.MLMock;
import domain.Organizacion;
import domain.entidades.EntidadBase;
import domain.operaciones.*;
import domain.repositorios.RepositorioEgresos;
import domain.usuarios.Estandar;
import domain.usuarios.TipoUsuario;
import domain.usuarios.Usuario;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CrearEgresos {

    public CrearEgresos() {

        EgresoBuilder builder;
        RepositorioEgresos repoEgresos = RepositorioEgresos.getInstance();

        List<Item> items = new ArrayList<>();
        Item item = new Item("Television 4k");
        item.setValor(new BigDecimal(10));
        Item item2 = new Item("Television led");
        item2.setValor(new BigDecimal(15));
        items.add(item);
        items.add(item2);

        builder = new EgresoBuilder();
        builder.agregarItems(items);
        builder.setEtiqueta("Inmuebles");
        builder.build();
        builder.agregarPresupuesto(items);
        builder.getEgreso().realizarValidaciones();
    }

}
