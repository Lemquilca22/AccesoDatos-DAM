package model;

public class DetallePedido {
    private int idPedido;
    private int idProducto;
    private int cantidad;
    private double precioUnitario;

    public DetallePedido(int idPedido, int id_producto, int cantidad, double precioUnitario) {
        this.idPedido = idPedido;
        this.idProducto = id_producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
    public double calcularSubtotal() {
        return this.cantidad * this.precioUnitario;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getId_producto() {
        return idProducto;
    }

    public void setId_producto(int id_producto) {
        this.idProducto = id_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    @Override
    public String toString() {
        return "DetallesPedido [Pedido=" + idPedido + ", Producto=" + idProducto +
                ", Cantidad=" + cantidad + ", Precio Unitario=" + precioUnitario +
                "€, Subtotal=" + calcularSubtotal() + "€]";
    }
}
