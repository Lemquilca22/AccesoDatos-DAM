package dao;

import model.Producto;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ProductoDao {
    private static final Path RUTA_INVENTARIO = Paths.get("data/productos/inventario.dat");

    public static void guardarInventario(List<Producto> productos) {
        try {
            Files.createDirectories(RUTA_INVENTARIO.getParent());
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new BufferedOutputStream(Files.newOutputStream(RUTA_INVENTARIO)))) {
                oos.writeObject(new ArrayList<>(productos));
            }
            AuditLogger.registrarLog("SUCCESS", "Inventario guardado: " + productos.size() + " productos.");

        } catch (IOException e) {
            AuditLogger.registrarLog("ERROR", "Error al guardar el inventario: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public static List<Producto> cargarInventario() {
        if (!Files.exists(RUTA_INVENTARIO)) {
            AuditLogger.registrarLog("WARNING", "No existe el archivo inventario.dat");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(RUTA_INVENTARIO)))) {
            Object leido = ois.readObject();
            List<Producto> productos = (List<Producto>) leido;
            AuditLogger.registrarLog("INFO", "Inventario cargado: " + productos.size() + " productos.");
            return productos;

        } catch (ClassNotFoundException e) {
            AuditLogger.registrarLog("ERROR", "Producto no encontrado " + e.getMessage());
            return new ArrayList<>();
        } catch (IOException e) {
            AuditLogger.registrarLog("ERROR", "Error al leer el archivo inventario.dat " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
