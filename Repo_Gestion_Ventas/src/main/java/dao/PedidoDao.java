package dao;

import model.Pedido;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDao {
    private static final Path RUTA_ARCHIVO = Paths.get("data/pedidos/pedidos.dat");

    public static void guardarPedidos(List<Pedido> pedidos) {
        try {
            Files.createDirectories(RUTA_ARCHIVO.getParent());
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(RUTA_ARCHIVO))) {
                oos.writeObject(pedidos);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar los pedidos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Pedido> cargarPedidos() {
        if (!Files.exists(RUTA_ARCHIVO)) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(RUTA_ARCHIVO))) {
            return (List<Pedido>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al leer los pedidos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}