package dao;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;

public class DirectoryManager {

    public static void iniciarEstructura() throws IOException {
        // Rutas
        Path dataPath = Paths.get("data");
        Path logsPath = Paths.get("data/logs");
        Path backupPath = Paths.get("data/backup");
        Path productosPath = Paths.get("data/productos");

        Files.createDirectories(dataPath);
        Files.createDirectories(logsPath);
        Files.createDirectories(backupPath);
        Files.createDirectories(productosPath);

        System.out.println("Estructura de directorios './data' creada correctamente.");
    }

    // Método de inspección
    public static void inspeccionarDirectorio(String rutaStr) {
        Path ruta = Paths.get(rutaStr);
        try {
            System.out.println("\n--- 🔍 INSPECCIÓN DE DIRECTORIO ---");
            System.out.println("Ruta Absoluta: " + ruta.toAbsolutePath());
            System.out.println("¿Existe físicamente?: " + Files.exists(ruta));
            System.out.println("Permisos - Es legible: " + Files.isReadable(ruta));
            System.out.println("Permisos - Es escribible: " + Files.isWritable(ruta));

            if (Files.exists(ruta)) {
                // Fecha de última modificación
                FileTime ultimaMod = Files.getLastModifiedTime(ruta);
                System.out.println("Fecha de última modificación: " + ultimaMod);

                // Espacio disponible en el disco duro donde se aloja la carpeta
                FileStore store = Files.getFileStore(ruta);
                long espacioDisponibleGB = store.getUsableSpace() / (1024 * 1024 * 1024);
                System.out.println("Espacio disponible en disco: " + espacioDisponibleGB + " GB");
            }
        } catch (IOException e) {
            System.err.println("Error al inspeccionar el directorio: " + e.getMessage());
        }
    }

}