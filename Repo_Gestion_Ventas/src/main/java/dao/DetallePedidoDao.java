package dao;

import model.DetallePedido;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DetallePedidoDao {
    private static final Path RUTA_ARCHIVO = Paths.get("data/pedidos/detalles.dat");

    public static void guardarDetalles(List<DetallePedido> detalles) {
        try {
            Files.createDirectories(RUTA_ARCHIVO.getParent());
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(RUTA_ARCHIVO))) {
                oos.writeObject(detalles);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar los detalles: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<DetallePedido> cargarDetalles() {
        if (!Files.exists(RUTA_ARCHIVO)) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(RUTA_ARCHIVO))) {
            return (List<DetallePedido>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al leer los detalles: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}