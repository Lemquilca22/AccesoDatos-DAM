package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Pedido tal y como está en la tabla {@code pedido} de MySQL (RA2), junto con sus líneas
 * (tabla {@code pedido_producto}, que se representan con {@link DetallePedido}).
 * <p>
 * No sustituye a {@link Pedido}, que se mantiene para RA1.
 */
public class PedidoBD {

    private int idPedido;               // id_pedido (AUTO_INCREMENT)
    private int idUsuario;              // id_usuario (FK)
    private LocalDateTime fechaPedido;  // fecha_pedido DATETIME
    private double precioTotal;         // precio_total DECIMAL(10,2)
    private List<DetallePedido> lineas = new ArrayList<>();

    /** Para leer filas de la BD (ya tienen id). */
    public PedidoBD(int idPedido, int idUsuario, LocalDateTime fechaPedido, double precioTotal) {
        this.idPedido = idPedido;
        this.idUsuario = idUsuario;
        this.fechaPedido = fechaPedido;
        this.precioTotal = precioTotal;
    }

    /** Para crear un pedido nuevo: el id lo genera MySQL y el total sale de las líneas. */
    public PedidoBD(int idUsuario, LocalDateTime fechaPedido) {
        this(0, idUsuario, fechaPedido, 0);
    }

    /** Añade una línea al pedido y actualiza el total. */
    public void addLinea(DetallePedido linea) {
        lineas.add(linea);
        precioTotal += linea.calcularSubtotal();
    }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }

    public double getPrecioTotal() { return precioTotal; }
    public void setPrecioTotal(double precioTotal) { this.precioTotal = precioTotal; }

    public List<DetallePedido> getLineas() { return lineas; }

    @Override
    public String toString() {
        return "Pedido [ID=" + idPedido + ", ID Usuario=" + idUsuario + ", Fecha=" + fechaPedido +
                ", Total=" + precioTotal + "€, Líneas=" + lineas.size() + "]";
    }
}
