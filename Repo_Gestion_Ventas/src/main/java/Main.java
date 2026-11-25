import dao.AccesoDirectoProductos;
import dao.AuditLogger;
import dao.DirectoryManager;
import dao.ProductoDao;
import daoJDBC.ProductoDaoJDBC;
import model.Producto;
import model.ProductoBD;
import util.DatosPrueba;

import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println(Path.of("").toAbsolutePath());

        ProductoBD p = new ProductoBD("Webcam HD", 39.90, true);
        int id = ProductoDaoJDBC.insertarDatosIndividual(p);
        System.out.println("Insertado con id: " + id);

        p.setPrecio(34.90);
        p.setDisponible(false);
        System.out.println("Actualizado: " + ProductoDaoJDBC.actualizar(p));

        System.out.println("Eliminado: " + ProductoDaoJDBC.eliminar(id));
    }


}
