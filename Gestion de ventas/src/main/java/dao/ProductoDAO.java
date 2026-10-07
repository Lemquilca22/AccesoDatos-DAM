package dao;

import model.Producto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProductoDAO {
    private final Map<Integer, Producto> tabla = new LinkedHashMap<>();
    private int siguienteId = 1;

    public Producto insertar(Producto producto) {
        producto.setIdProducto(siguienteId++);
        tabla.put(producto.getIdProducto(), producto);
        return producto;
    }

    public Producto obtenerPorId(int idProducto) {
        return tabla.get(idProducto);
    }

    public List<Producto> buscarPorNombre(String texto) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : tabla.values()) {
            if (p.getNombreProducto().toLowerCase().contains(texto.toLowerCase())) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public List<Producto> obtenerTodos() {
        return new ArrayList<>(tabla.values());
    }

    public List<Producto> obtenerDisponibles() {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : tabla.values()) {
            if (p.isDisponible()) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public boolean actualizar(Producto producto) {
        if (!tabla.containsKey(producto.getIdProducto())) {
            return false;
        }
        tabla.put(producto.getIdProducto(), producto);
        return true;
    }

    public boolean eliminar(int idProducto) {
        return tabla.remove(idProducto) != null;
    }
}
