package dao;

import config.DatabaseConnection;
import model.EstadoPedido;
import model.Pedido;
import model.PedidoProducto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public Pedido insertar(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedido (id_usuario, fecha, estado) VALUES (?, ?, ?)";
        String sqlLinea = "INSERT INTO pedido_producto (id_pedido, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Transacción manual

            try (PreparedStatement pstmt = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, pedido.getIdUsuario());
                pstmt.setTimestamp(2, Timestamp.valueOf(pedido.getFecha()));
                pstmt.setString(3, pedido.getEstado().name());
                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        pedido.setIdPedido(rs.getInt(1));
                    }
                }
            }

            try (PreparedStatement pstmtLinea = conn.prepareStatement(sqlLinea)) {
                for (PedidoProducto linea : pedido.getLineas()) {
                    linea.setIdPedido(pedido.getIdPedido());
                    pstmtLinea.setInt(1, linea.getIdPedido());
                    pstmtLinea.setInt(2, linea.getIdProducto());
                    pstmtLinea.setInt(3, linea.getCantidad());
                    pstmtLinea.setBigDecimal(4, linea.getPrecioUnitario());
                    pstmtLinea.addBatch();
                }
                pstmtLinea.executeBatch();
            }

            conn.commit(); // Confirmar cambios
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
        return pedido;
    }

    public Pedido obtenerPorId(int idPedido) {
        String sql = "SELECT * FROM pedido WHERE id_pedido = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPedido);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Pedido p = mapearPedido(rs);
                    p.setLineas(obtenerLineasPorPedido(p.getIdPedido(), conn));
                    return p;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Pedido> obtenerTodos() {
        List<Pedido> resultado = new ArrayList<>();
        String sql = "SELECT * FROM pedido";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pedido p = mapearPedido(rs);
                p.setLineas(obtenerLineasPorPedido(p.getIdPedido(), conn));
                resultado.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public List<Pedido> obtenerPorUsuario(int idUsuario) {
        List<Pedido> resultado = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE id_usuario = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Pedido p = mapearPedido(rs);
                    p.setLineas(obtenerLineasPorPedido(p.getIdPedido(), conn));
                    resultado.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public boolean productoEnUso(int idProducto) {
        String sql = "SELECT COUNT(*) FROM pedido_producto WHERE id_producto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProducto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarEstado(int idPedido, EstadoPedido estado) {
        String sql = "UPDATE pedido SET estado = ? WHERE id_pedido = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, estado.name());
            pstmt.setInt(2, idPedido);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean eliminar(int idPedido) {
        String sql = "DELETE FROM pedido WHERE id_pedido = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPedido);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setIdPedido(rs.getInt("id_pedido"));
        p.setIdUsuario(rs.getInt("id_usuario"));

        Timestamp ts = rs.getTimestamp("fecha");
        if (ts != null) {
            p.setFecha(ts.toLocalDateTime());
        }

        p.setEstado(EstadoPedido.valueOf(rs.getString("estado")));
        return p;
    }

    private List<PedidoProducto> obtenerLineasPorPedido(int idPedido, Connection conn) throws SQLException {
        List<PedidoProducto> lineas = new ArrayList<>();
        String sql = "SELECT * FROM pedido_producto WHERE id_pedido = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPedido);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PedidoProducto pp = new PedidoProducto();
                    pp.setIdPedido(rs.getInt("id_pedido"));
                    pp.setIdProducto(rs.getInt("id_producto"));
                    pp.setCantidad(rs.getInt("cantidad"));
                    pp.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
                    lineas.add(pp);
                }
            }
        }
        return lineas;
    }
}