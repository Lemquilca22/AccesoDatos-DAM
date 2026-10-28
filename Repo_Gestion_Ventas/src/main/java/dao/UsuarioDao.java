package dao;

import model.Usuario;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {
    private static final Path RUTA_ARCHIVO = Paths.get("data/usuarios/usuarios.dat");

    public static void guardarUsuarios(List<Usuario> usuarios) {
        try {
            Files.createDirectories(RUTA_ARCHIVO.getParent());
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(RUTA_ARCHIVO))) {
                oos.writeObject(usuarios);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar los usuarios: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Usuario> cargarUsuarios() {
        if (!Files.exists(RUTA_ARCHIVO)) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(RUTA_ARCHIVO))) {
            return (List<Usuario>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al leer los usuarios: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}