package model;
import java.math.BigDecimal;

public class Producto {
    private int idProducto;
    private String nombreProducto;
    private BigDecimal precio;
    private boolean disponible;


    public Producto() {}

    public Producto(String nombreProducto, BigDecimal precio, boolean disponible) {
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.disponible = disponible;
    }

    public Producto(int idProducto, String nombreProducto, BigDecimal precio, boolean disponible) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.disponible = disponible;
    }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }


}
