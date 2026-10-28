package dao;

import model.Producto;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AccesoDirectoProductos {
    private static final Path RUTA_ARCHIVO = Paths.get("data/productos/productos_directo.dat");

    private static final int TAMANIO_NOMBRE = 30;
    private static final int TAMANIO_REGISTRO = 4 + (TAMANIO_NOMBRE * 2) + 8 + 4;

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
        }
    }
    // Entender hasta aquí

    public static void actualizarStockDirecto(long posicionRegistro, int nuevoStock) {
        try (RandomAccessFile raf = new RandomAccessFile(RUTA_ARCHIVO.toFile(), "rw")) {
            long byteOffsetStock = (posicionRegistro * TAMANIO_REGISTRO) + 4 + (TAMANIO_NOMBRE * 2) + 8;

            raf.seek(byteOffsetStock);
            raf.writeInt(nuevoStock);

            System.out.println("Stock del producto en posición " + posicionRegistro + " actualizado directamente a: " + nuevoStock);
        } catch (IOException e) {
            System.err.println("Error al actualizar el stock directamente: " + e.getMessage());
        }
    }

    public static Producto leerProducto(long posicionRegistro) {
        try (RandomAccessFile raf = new RandomAccessFile(RUTA_ARCHIVO.toFile(), "r")) {
            long byteInicio = posicionRegistro * TAMANIO_REGISTRO;

            if (byteInicio >= raf.length()) {
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

        } catch (IOException e) {
            System.err.println("Error al leer por acceso directo: " + e.getMessage());
            return null;
        }
    }
}