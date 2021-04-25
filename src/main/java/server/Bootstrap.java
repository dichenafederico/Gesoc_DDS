package server;

import domain.APIs.MLMock;
import domain.Organizacion;
import domain.entidades.*;
import domain.operaciones.MedioDePago;
import domain.operaciones.NombrePais;
import domain.operaciones.Proveedor;
import domain.operaciones.TipoDePago;
import domain.usuarios.Estandar;
import domain.usuarios.Usuario;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import java.math.BigDecimal;

public class Bootstrap implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

    public static void main(String[] args) {
        new Bootstrap().init();
    }

    public void init() {
        MLMock api = new MLMock();
        String contraseniaValida = "Contraseni@Valida123";

        /*setUP , NO LO PONGO en la transaccion porque LA ROMPE*/
        Organizacion org2 = new Organizacion();
        EntidadBase entidadBase = new EntidadBase("Entidad 1(base y A)", org2, new BigDecimal(1000));
        EntidadJuridica entidadJuridica = new EntidadJuridica("Entidad 2(juridica y B)", org2, new BigDecimal(9000));
        CategoriaEntidad categoria1 = new CategoriaEntidad("A");
        CategoriaEntidad categoria2 = new CategoriaEntidad("B");
        entidadBase.setCategoriaEntidad(categoria1);
        entidadJuridica.setCategoriaEntidad(categoria2);
        org2.agregarComandoAcategoria(Comando.BLOQUEAR_ENTIDAD_BASE_PARA_JURIDICA.getDescripcion(), "A");
        org2.agregarComandoAcategoria(Comando.NO_AGREGAR_MAS_ENTIDADES_BASE.getDescripcion(), "A");
        org2.agregarComandoAcategoria(Comando.NO_ACEPTAR_MAS_EGRESOS.getDescripcion(), "A");

        org2.agregarComandoAcategoria(Comando.NO_ACEPTAR_MAS_EGRESOS.getDescripcion(), "B");


        Usuario axel = new Usuario("Axel", contraseniaValida, new Estandar());
        Usuario eB = new Usuario("Empleado base", contraseniaValida, new Estandar());
        axel.setEntidad(entidadBase);
        eB.setEntidad(entidadBase);
        Usuario alex = new Usuario("Alex", contraseniaValida, new Estandar());
        Usuario eP = new Usuario("Empleado juridico", contraseniaValida, new Estandar());
        alex.setEntidad(entidadJuridica);
        eP.setEntidad(entidadJuridica);

        Organizacion org = new Organizacion();
        Entidad entidad = new EntidadBase("prueba", org, BigDecimal.ONE);
        Usuario juan = new Usuario("Juan", contraseniaValida, new Estandar());

        entidadJuridica.agregarEntidadBase(entidadBase);

        withTransaction(() -> {
            persist(eB);
            persist(eP);
            persist(alex);
            persist(axel);
            persist(categoria1);
            persist(categoria2);
            persist(entidadBase);
            persist(entidadJuridica);
            /*cosas de entidades aca arriba , lo separo para no confundir*/

            org.agregarEtiqueta("Muebles");
            org.agregarEtiqueta("Electrodomesticos");
            persist(org);
            persist(entidad);
            Usuario carlos = new Usuario("Carlos", contraseniaValida, new Estandar());
            carlos.setEntidad(entidad);
            juan.setEntidad(entidad);
            persist(juan);
            persist(carlos);
            MedioDePago visa = new MedioDePago("Visa", TipoDePago.TARJETA_CREDITO);
            MedioDePago mastercard = new MedioDePago("Mastercard", TipoDePago.TARJETA_CREDITO);
            persist(visa);
            persist(mastercard);
            Proveedor proveedor = new Proveedor(api, NombrePais.ARGENTINA, "1407", "Sarachaga", 234, 1, "a", 204013552, "Proveedor1", "Prueba");
            Proveedor proveedor2 = new Proveedor(api, NombrePais.ARGENTINA, "1407", "Sarachaga", 5416, 1, "a", 204013551, "Proveedor2", "Prueba");
            persist(proveedor);
            persist(proveedor2);
            //TODO: agregar usuarios,organizaciones,entidades,categorias,proveedores,etc
            //todo lo que se necesite para la creacion de los egresos y entidades

        });

        /*List<Item> items = new ArrayList<>();
        Item item = new Item("Television 4k");
        item.setValor(new BigDecimal(10));
        Item item2 = new Item("Television led");
        item2.setValor(new BigDecimal(15));
        items.add(item);
        items.add(item2);
        EgresoBuilder builder = new EgresoBuilder();
        builder.agregarItems(items);
        builder.setOrganizacion(org);
        builder.setUsuarioVerificador(juan);
        builder.setEntidad(entidad);
        builder.setEtiqueta("Electrodomesticos");
        builder.build();/*
        builder.agregarPresupuesto(items);*/

    }
}
