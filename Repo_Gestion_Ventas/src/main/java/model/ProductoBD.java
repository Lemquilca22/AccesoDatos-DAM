package model;

/**
 * Producto tal y como está en la tabla {@code producto} de MySQL (RA2).
 * <p>
 * No sustituye a {@link Producto}: esa clase la siguen usando las fases de ficheros de RA1
 * (serialización y acceso directo), que necesitan stock y categoría.
 */
public class ProductoBD {

    private int idProducto;          // id_producto (AUTO_INCREMENT)
    private String nombreProducto;   // nombre_producto
    private double precio;           // precio DECIMAL(10,2)
    private boolean disponible;      // disponible BOOLEAN

    /** Para leer filas de la BD (ya tienen id). */
    public ProductoBD(int idProducto, String nombreProducto, double precio, boolean disponible) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.disponible = disponible;
    }

    /** Para insertar: el id lo genera MySQL. */
    public ProductoBD(String nombreProducto, double precio, boolean disponible) {
        this(0, nombreProducto, precio, disponible);
    }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public String toString() {
        return "Producto [ID=" + idProducto + ", Nombre=" + nombreProducto + ", Precio=" + precio +
                "€, Disponible=" + (disponible ? "Sí" : "No") + "]";
    }
}
