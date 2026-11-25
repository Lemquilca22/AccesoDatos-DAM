package daoJDBC;

import dao.ConexionDB;
import model.Producto;
import model.ProductoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        String sql = "DELETE FROM producto WHERE id_producto=?";

        try (Connection conexion =  ConexionDB.getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar datos: " + e.getMessage());
            return false;
        }
    }

    public static boolean actualizar (ProductoBD producto) {
        String sql = "UPDATE producto SET nombre_producto = ?, precio= ?, disponible=? WHERE id_producto=?";

        try (Connection conexion = ConexionDB.getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, producto.getNombreProducto());
            ps.setDouble(2, producto.getPrecio());
            ps.setBoolean(3, producto.isDisponible());
            ps.setInt(4, producto.getIdProducto());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar datos: " + e.getMessage());
            return false;
        }
    }

    public static List<ProductoBD> listarTodos() {
        List<ProductoBD> lista = new ArrayList<>();
        String sql = "SELECT id_producto, nombre_producto, precio, disponible FROM producto";

        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new ProductoBD(
                        rs.getInt("id_producto"),
                        rs.getString("nombre_producto"),
                        rs.getDouble("precio"),
                        rs.getBoolean("disponible")));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    public static ProductoBD buscarPorId(int idProducto) {
        String sql = "SELECT id_producto, nombre_producto, precio, disponible FROM producto WHERE id_producto = ?";

        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ProductoBD(
                            rs.getInt("id_producto"),
                            rs.getString("nombre_producto"),
                            rs.getDouble("precio"),
                            rs.getBoolean("disponible"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar producto: " + e.getMessage());
        }
        return null;
    }
}
