package service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GestorDirectorios {

    public void iniciarEstructura() throws Exception{

        Path rutaUsuarios = Paths.get("tienda/usuraios");
        Path rutaProductos = Paths.get("tienda/productos");
        Path rutaPedidos =  Paths.get("tienda/pedidos");
        Path rutaLogs =  Paths.get("tienda/logs");
        Path rutaConversiones =  Paths.get("tienda/conversiones");
        Path rutabackup =   Paths.get("tienda/backup");

        Files.createDirectories(rutaUsuarios);
        Files.createDirectories(rutaProductos);
        Files.createDirectories(rutaPedidos);
        Files.createDirectories(rutaLogs);
        Files.createDirectories(rutaConversiones);

        System.out.println("Directorios creados correctamente");
    }
}
