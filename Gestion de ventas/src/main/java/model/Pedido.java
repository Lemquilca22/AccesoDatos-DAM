package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int idPedido;
    private int idUsuario;
    private LocalDateTime fecha;
    private EstadoPedido estado;
    private List<PedidoProducto> lineas = new ArrayList<>();

    public Pedido() {}

    public Pedido(int idUsuario) {
        this.idUsuario = idUsuario;
        this.fecha = LocalDateTime.now();
        this.estado = EstadoPedido.PENDIENTE;
    }

    public Pedido(int idPedido, int idUsuario, LocalDateTime fecha, EstadoPedido estado) {
        this.idPedido = idPedido;
        this.idUsuario = idUsuario;
        this.fecha = fecha;
        this.estado = estado;
    }

    public void addLinea(PedidoProducto nueva) {
        for (PedidoProducto linea : lineas) {
            if (linea.getIdProducto() == nueva.getIdProducto()) {
                linea.setCantidad(linea.getCantidad() + nueva.getCantidad());
                return;
            }
        }
        lineas.add(nueva);
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (PedidoProducto linea : lineas) {
            total = total.add(linea.getSubtotal());
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

    public List<PedidoProducto> getLineas() { return lineas; }
    public void setLineas(List<PedidoProducto> lineas) { this.lineas = lineas; }
}
