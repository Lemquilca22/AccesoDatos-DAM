package model;

import java.io.Serializable;

public class DetallePedido implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idPedido;
    private int idProducto;
    private int cantidad;
    private double precioUnitario;

    public DetallePedido(int idPedido, int idProducto, int cantidad, double precioUnitario) {
        this.idPedido = idPedido;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public double calcularSubtotal() {
        return this.cantidad * this.precioUnitario;
    }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    @Override
    public String toString() {
        return "DetallesPedido [Pedido=" + idPedido + ", Producto=" + idProducto +
                ", Cantidad=" + cantidad + ", Precio Unitario=" + precioUnitario +
                "€, Subtotal=" + calcularSubtotal() + "€]";
    }
}