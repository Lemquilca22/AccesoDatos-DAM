package dao;

import model.EstadoPedido;
import model.Pedido;
import model.PedidoProducto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PedidoDAO {
    private final Map<Integer, Pedido> tabla = new LinkedHashMap<>();
    private int siguienteId = 1;

    public Pedido insertar(Pedido pedido) {
        pedido.setIdPedido(siguienteId++);
        for (PedidoProducto linea : pedido.getLineas()) {
            linea.setIdPedido(pedido.getIdPedido());
        }
        tabla.put(pedido.getIdPedido(), pedido);
        return pedido;
    }

    public Pedido obtenerPorId(int idPedido) {
        return tabla.get(idPedido);
    }

    public List<Pedido> obtenerTodos() {
        return new ArrayList<>(tabla.values());
    }

    public List<Pedido> obtenerPorUsuario(int idUsuario) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido p : tabla.values()) {
            if (p.getIdUsuario() == idUsuario) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public boolean productoEnUso(int idProducto) {
        for (Pedido p : tabla.values()) {
            for (PedidoProducto linea : p.getLineas()) {
                if (linea.getIdProducto() == idProducto) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean actualizarEstado(int idPedido, EstadoPedido estado) {
        Pedido pedido = tabla.get(idPedido);
        if (pedido == null) {
            return false;
        }
        pedido.setEstado(estado);
        return true;
    }

    public boolean eliminar(int idPedido) {
        return tabla.remove(idPedido) != null;
    }
}
