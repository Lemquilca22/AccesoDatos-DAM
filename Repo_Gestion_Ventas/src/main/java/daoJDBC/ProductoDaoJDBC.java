package daoJDBC;

import dao.ConexionDB;
import model.Producto;
import model.ProductoBD;

import java.sql.*;

public class ProductoDaoJDBC {

    public static int insertarDatosIndividual (ProductoBD producto) {
        String sql = "INSERT INTO producto (nombre_producto, precio, disponible) VALUES (?, ?, ?)";

        try (Connection conexion = ConexionDB.getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql,  Statement.RETURN_GENERATED_KEYS))  {

            ps.setString(1, producto.getNombreProducto());
            ps.setDouble(2, producto.getPrecio());
            ps.setBoolean(3, producto.isDisponible());
            ps.executeUpdate();

            try  (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    producto.setIdProducto(id);
                    return id;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al realizar los INSERTS " + e.getMessage());
        }
        return -1;
    }

    public static boolean eliminar (int idProducto) {
        String sql = "DELETE FROM producto  WHERE idProducto=?";

        try (Connection conexion =  ConexionDB.getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar datos: " + e.getMessage());
            return false;
        }
    }
}
