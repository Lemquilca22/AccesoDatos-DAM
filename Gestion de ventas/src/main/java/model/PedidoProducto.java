package model;

import java.math.BigDecimal;

public class PedidoProducto {
    private int idPedido;
    private int idProducto;
    private int cantidad;
    private BigDecimal precioUnitario;

    public PedidoProducto() {}

    public PedidoProducto(int idProducto, int cantidad, BigDecimal precioUnitario) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public PedidoProducto(int idPedido, int idProducto, int cantidad, BigDecimal precioUnitario) {
        this.idPedido = idPedido;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
}
