package dao;

import model.Producto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ProductoDao {
    private static final Path RUTA_ARCHIVO = Paths.get("data/productos/inventario.dat");

    public static void guardarInventario(List<Producto> productos){
        try{
            Files.createDirectories(RUTA_ARCHIVO.getParent());
            try(ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(RUTA_ARCHIVO))) {
                oos.writeObject(productos);
                System.out.println("Inventario de productos guardado exitosamente en binario");
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el inventario en el disco: " + e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    public static List<Producto> cargarInventario(){
        if (!Files.exists(RUTA_ARCHIVO)){
            System.out.println("El archivo inventario.dat no existe todavía");
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(RUTA_ARCHIVO))) {
            List<Producto> productos = (List<Producto>) ois.readObject();
            System.out.println("📂 Inventario cargado con éxito desde el archivo binario.");
            return productos;
        } catch (IOException | ClassNotFoundException e){
            System.out.println("Error al leer el archivo binario de inventario");
            return new ArrayList<>();
        }
    }
}


