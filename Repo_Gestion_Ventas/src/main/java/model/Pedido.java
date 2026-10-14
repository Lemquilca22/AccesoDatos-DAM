package model;
import java.time.LocalDateTime;

public class Pedido {
    private int idPedido;
    private int idUsuario;
    private LocalDateTime fecha;
    private EstadoPedido estado;

    public Pedido(int idPedido, int idUsuario, LocalDateTime fecha, EstadoPedido estado) {
        this.idPedido = idPedido;
        this.idUsuario = idUsuario;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Pedido [ID=" + idPedido +
                ", ID Usuario=" + idUsuario +
                ", Fecha=" + fecha +
                ", Estado=" + estado + "]";
    }
}
