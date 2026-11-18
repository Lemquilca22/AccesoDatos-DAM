package dao;

import model.Producto;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AccesoDirectoProductos {
    private static final Path RUTA_ARCHIVO = Paths.get("data/productos/productos_directo.dat");

    private static final int TAMANIO_NOMBRE = 30;
    private static final int TAMANIO_REGISTRO = 4 + (TAMANIO_NOMBRE * 2) + 8 + 4;

    private static final int OFFSET_STOCK = 4 + (TAMANIO_NOMBRE * 2) + 8;

    public static void escribirProducto(Producto p, long posicionRegistro) {
        try {
            Files.createDirectories(RUTA_ARCHIVO.getParent());

            try (RandomAccessFile raf = new RandomAccessFile(RUTA_ARCHIVO.toFile(), "rw")) {
                raf.seek(posicionRegistro * TAMANIO_REGISTRO);

                raf.writeInt(p.getId());

                StringBuffer sb = new StringBuffer(p.getNombre());
                sb.setLength(TAMANIO_NOMBRE);
                raf.writeChars(sb.toString());

                raf.writeDouble(p.getPrecio());

                raf.writeInt(p.getStock());

                System.out.println("Producto escrito por acceso directo en la posición: " + posicionRegistro);
            }
        } catch (IOException e) {
            System.err.println("Error al escribir por acceso directo: " + e.getMessage());
            AuditLogger.registrarLog("ERROR", "Fallo al escribir el registro " + posicionRegistro + ": " + e.getMessage());
        }
    }
    // Entender hasta aquí

    public static void guardarTodos(List<Producto> productos) {
        try {
            Files.createDirectories(RUTA_ARCHIVO.getParent());
            try (RandomAccessFile raf = new RandomAccessFile(RUTA_ARCHIVO.toFile(), "rw")) {
                raf.setLength(0);
            }
        } catch (IOException e) {
            System.err.println("Error al preparar el fichero de acceso directo: " + e.getMessage());
            return;
        }

        for (int i = 0; i < productos.size(); i++) {
            escribirProducto(productos.get(i), i);
        }
        AuditLogger.registrarLog("SUCCESS", "Fichero de acceso directo generado con " + productos.size() + " registros.");
    }

    public static long numeroRegistros() {
        if (!Files.exists(RUTA_ARCHIVO)) {
            return 0;
        }
        try (RandomAccessFile raf = new RandomAccessFile(RUTA_ARCHIVO.toFile(), "r")) {
            return raf.length() / TAMANIO_REGISTRO;
        } catch (IOException e) {
            System.err.println("Error al calcular el número de registros: " + e.getMessage());
            return 0;
        }
    }

    public static boolean actualizarStockDirecto(long posicionRegistro, int nuevoStock) {
        if (!Files.exists(RUTA_ARCHIVO)) {
            System.err.println("No existe el fichero de acceso directo: " + RUTA_ARCHIVO);
            return false;
        }
        if (posicionRegistro < 0) {
            System.err.println("La posición no puede ser negativa.");
            return false;
        }

        try (RandomAccessFile raf = new RandomAccessFile(RUTA_ARCHIVO.toFile(), "rw")) {
            if ((posicionRegistro + 1) * TAMANIO_REGISTRO > raf.length()) {
                System.err.println("La posición " + posicionRegistro + " está fuera de los límites del archivo.");
                return false;
            }

            long byteOffsetStock = (posicionRegistro * TAMANIO_REGISTRO) + OFFSET_STOCK;

            raf.seek(byteOffsetStock);
            raf.writeInt(nuevoStock);

            System.out.println("Stock del producto en posición " + posicionRegistro + " actualizado directamente a: " + nuevoStock);
            AuditLogger.registrarLog("INFO", "Stock del registro " + posicionRegistro + " actualizado a " + nuevoStock);
            return true;
        } catch (IOException e) {
            System.err.println("Error al actualizar el stock directamente: " + e.getMessage());
            AuditLogger.registrarLog("ERROR", "Fallo al actualizar el stock del registro " + posicionRegistro + ": " + e.getMessage());
            return false;
        }
    }
    //Analizamos proxima clasee

    public static Producto leerProducto(long posicionRegistro) {
        try (RandomAccessFile raf = new RandomAccessFile(RUTA_ARCHIVO.toFile(), "r")) {
            long byteInicio = posicionRegistro * TAMANIO_REGISTRO;

            if (posicionRegistro < 0 || byteInicio + TAMANIO_REGISTRO > raf.length()) {
                System.out.println("La posición " + posicionRegistro + " está fuera de los límites del archivo.");
                return null;
            }

            raf.seek(byteInicio);

            int id = raf.readInt();

            char[] charsNombre = new char[TAMANIO_NOMBRE];
            for (int i = 0; i < TAMANIO_NOMBRE; i++) {
                charsNombre[i] = raf.readChar();
            }
            String nombre = new String(charsNombre).trim();

            double precio = raf.readDouble();
            int stock = raf.readInt();

            return new Producto(id, nombre, precio, stock, "General");

        } catch (FileNotFoundException e) {
            System.err.println("No existe el fichero de acceso directo: " + RUTA_ARCHIVO);
            return null;
        } catch (IOException e) {
            System.err.println("Error al leer por acceso directo: " + e.getMessage());
            return null;
        }
    }

    public static List<Producto> leerTodos() {
        List<Producto> productos = new ArrayList<>();
        long total = numeroRegistros();
        for (long i = 0; i < total; i++) {
            Producto p = leerProducto(i);
            if (p != null) {
                productos.add(p);
            }
        }
        return productos;
    }
}
