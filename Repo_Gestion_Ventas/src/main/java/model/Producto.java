package model;
import java.io.Serializable;
public class Producto {
    private int idProducto;
    private String nombreProducto;
    private double precio;
    private boolean disponible;

    public Producto(int idProducto, String nombreProducto, double precio, boolean disponible) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.disponible = disponible;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Producto [ID=" + idProducto + ", Nombre=" + nombreProducto +
                ", Precio=" + precio + "€, Disponible=" + (disponible ? "Sí" : "No") + "]";
    }
}
