package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Pedido tal y como está en la tabla {@code pedido} de MySQL (RA2), junto con sus líneas
 * (tabla {@code pedido_producto}, que se representan con {@link DetallePedido}).
 * <p>
 * La tabla no guarda el total: se calcula sumando las líneas ({@link #calcularTotal()}).
 * <p>
 * No sustituye a {@link Pedido}, que se mantiene para RA1.
 */
public class PedidoBD {

    private int idPedido;               // id_pedido (AUTO_INCREMENT)
    private int idUsuario;              // id_usuario (FK)
    private LocalDateTime fecha;        // fecha DATETIME
    private EstadoPedido estado;        // estado VARCHAR(50) -> se guarda con estado.name()
    private List<DetallePedido> lineas = new ArrayList<>();

    /** Para leer filas de la BD (ya tienen id). */
    public PedidoBD(int idPedido, int idUsuario, LocalDateTime fecha, EstadoPedido estado) {
        this.idPedido = idPedido;
        this.idUsuario = idUsuario;
        this.fecha = fecha;
        this.estado = estado;
    }

    /** Para crear un pedido nuevo: el id lo genera MySQL y empieza PENDIENTE. */
    public PedidoBD(int idUsuario, LocalDateTime fecha) {
        this(0, idUsuario, fecha, EstadoPedido.PENDIENTE);
    }

    public void addLinea(DetallePedido linea) {
        lineas.add(linea);
    }

    /** Total del pedido = suma de los subtotales de sus líneas. */
    public double calcularTotal() {
        double total = 0;
        for (DetallePedido linea : lineas) {
            total += linea.calcularSubtotal();
        }
        return total;
    }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public List<DetallePedido> getLineas() { return lineas; }

    @Override
    public String toString() {
        return "Pedido [ID=" + idPedido + ", ID Usuario=" + idUsuario + ", Fecha=" + fecha +
                ", Estado=" + estado + ", Total=" + calcularTotal() + "€, Líneas=" + lineas.size() + "]";
    }
}
