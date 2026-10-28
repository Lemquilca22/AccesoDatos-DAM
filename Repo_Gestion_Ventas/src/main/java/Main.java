import dao.AuditLogger;
import dao.DirectoryManager;
import dao.ProductoDao;
import model.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            System.out.println("=== [FASE 2] GESTIÓN Y CREACIÓN DE DIRECTORIOS ===");

            DirectoryManager.iniciarEstructura();

            DirectoryManager.inspeccionarDirectorio("data");

            System.out.println("\n=== [FASE 3] FLUIDOS DE TEXTO Y AUDITORÍA ===");

            AuditLogger.registrarLog("INFO", "Sistema de Gestión de Ventas inicializado con éxito.");
            AuditLogger.registrarLog("SUCCESS", "Directorios de productos, usuarios y pedidos verificados.");
            AuditLogger.registrarLog("WARNING", "Comprobando integridad del almacenamiento local.");


            AuditLogger.buscarPalabraClave("INFO");

            System.out.println("\n=== [FASE 4] SERIALIZACIÓN DE INVENTARIO ===");

            List<Producto> inventario = new ArrayList<>();
            inventario.add(new Producto(1, "Teclado mecánico", 59.99, 20, "Periféricos"));
            inventario.add(new Producto(2, "Monitor 27\"", 219.50, 8, "Pantallas"));
            inventario.add(new Producto(3, "Ratón inalámbrico", 24.90, 35, "Periféricos"));

            ProductoDao.guardarInventario(inventario);

            List<Producto> recuperados = ProductoDao.cargarInventario();
            recuperados.forEach(System.out::println);

            System.out.println("Funciona, SIUUUUUUUUUUUUUU");

        } catch (Exception e) {
            System.err.println("Error  en la ejecución del sistema: " + e.getMessage());
            e.printStackTrace();
        }
}
}
