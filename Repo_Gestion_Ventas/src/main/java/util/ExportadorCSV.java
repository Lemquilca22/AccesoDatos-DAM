package util;

import dao.AuditLogger;
import model.Producto;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Exporta el inventario de productos a formato CSV (Comma-Separated Values).
 * <p>
 * Es la "conversión de formatos" de la Fase 6: pasamos los objetos {@link Producto} que tenemos
 * serializados en binario ({@code inventario.dat}) a un fichero de texto plano
 * ({@code ./data/backup/inventario.csv}) que se puede abrir con Excel, LibreOffice o un bloc de notas.
 * <p>
 * Formato del fichero:
 * <pre>
 * id;nombre;precio;stock;categoria
 * 1;Teclado mecánico;59.99;20;Periféricos
 * </pre>
 * Usamos {@code ;} como separador porque el Excel en español usa la coma para los decimales,
 * y si separásemos con comas no compilaría
 */
public class ExportadorCSV {

    /** Ruta relativa del fichero CSV de copia de seguridad. */
    private static final Path RUTA_CSV = Paths.get("data/backup/inventario.csv");

    /** Primera línea del fichero: los nombres de las columnas. */
    private static final String CABECERA = "id;nombre;precio;stock;categoria";

    /**
     * Escribe la lista de productos en {@code ./data/backup/inventario.csv}.
     * <p>
     * Si el fichero ya existía se sobrescribe entero (no queremos un backup con datos duplicados).
     *
     * @param productos lista de productos a exportar
     */
    public static void exportarInventario(List<Producto> productos) {
        try {
            //Por si alguien ha borrado la carpeta backup a mano, la volvemos a crear
            Files.createDirectories(RUTA_CSV.getParent());

            //Notas para entender el codigo:
            //Aquí NO usamos APPEND como en el AuditLogger. Con CREATE + TRUNCATE_EXISTING lo que hacemos es
            //crear el fichero si no existe y, si existe, vaciarlo antes de escribir. Así cada exportación
            //es una foto nueva del inventario.
            try (BufferedWriter writer = Files.newBufferedWriter(RUTA_CSV, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {

                writer.write(CABECERA);
                writer.newLine(); //newLine() pone el salto de línea correcto según el sistema operativo

                for (Producto p : productos) {
                    //Si un nombre llevara un ';' se leería como una columna más, así que lo cambiamos por ','.
                    //El precio va con Double.toString() porque siempre usa punto (59.99) aunque el PC esté en español.
                    String linea = p.getId() + ";"
                            + p.getNombre().replace(";", ",") + ";"
                            + Double.toString(p.getPrecio()) + ";"
                            + p.getStock() + ";"
                            + p.getCategoria().replace(";", ",");
                    writer.write(linea);
                    writer.newLine();
                }
            }

            System.out.println("Inventario exportado a CSV: " + RUTA_CSV.toAbsolutePath());
            AuditLogger.registrarLog("SUCCESS", "Inventario exportado a CSV con " + productos.size() + " productos.");

        } catch (IOException e) {
            System.err.println("Error al exportar el inventario a CSV: " + e.getMessage());
            AuditLogger.registrarLog("ERROR", "Fallo al exportar a CSV: " + e.getMessage());
        }
    }
}
