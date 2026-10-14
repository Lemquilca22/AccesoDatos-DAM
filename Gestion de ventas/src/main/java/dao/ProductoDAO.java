package dao;

import config.DatabaseConnection;
import model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public Producto insertar(Producto producto) {
        String sql = "INSERT INTO producto (nombre_producto, precio, disponible) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, producto.getNombreProducto());
            pstmt.setBigDecimal(2, producto.getPrecio());
            pstmt.setBoolean(3, producto.isDisponible());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    producto.setIdProducto(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return producto;
    }

    public Producto obtenerPorId(int idProducto) {
        String sql = "SELECT * FROM producto WHERE id_producto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProducto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapearProducto(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Producto> buscarPorNombre(String texto) {
        List<Producto> resultado = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE LOWER(nombre_producto) LIKE LOWER(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + texto + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) resultado.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public List<Producto> obtenerTodos() {
        List<Producto> resultado = new ArrayList<>();
        String sql = "SELECT * FROM producto";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) resultado.add(mapearProducto(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public List<Producto> obtenerDisponibles() {
        List<Producto> resultado = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE disponible = TRUE";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) resultado.add(mapearProducto(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public boolean actualizar(Producto producto) {
        String sql = "UPDATE producto SET nombre_producto = ?, precio = ?, disponible = ? WHERE id_producto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, producto.getNombreProducto());
            pstmt.setBigDecimal(2, producto.getPrecio());
            pstmt.setBoolean(3, producto.isDisponible());
            pstmt.setInt(4, producto.getIdProducto());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean eliminar(int idProducto) {
        String sql = "DELETE FROM producto WHERE id_producto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProducto);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setIdProducto(rs.getInt("id_producto"));
        p.setNombreProducto(rs.getString("nombre_producto"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setDisponible(rs.getBoolean("disponible"));
        return p;
    }
}