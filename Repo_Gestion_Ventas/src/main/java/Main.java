import dao.AuditLogger;
import dao.DirectoryManager;

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

            System.out.println("\n¡Fases ejecutadas y validadas correctamente!");

        } catch (Exception e) {
            System.err.println("Error  en la ejecución del sistema: " + e.getMessage());
            e.printStackTrace();
        }
}
}
