-- Devuelve (como ResultSet, sin OUT) los pedidos de un día concreto
-- con el nombre del usuario y el total de cada pedido.
CREATE PROCEDURE pedidos_por_fecha(IN p_fecha DATE)
BEGIN
    SELECT p.id_pedido,
           u.nombre,
           p.fecha,
           p.estado,
           COALESCE(SUM(pp.cantidad * pp.precio_unitario), 0) AS total
    FROM pedido p
    JOIN usuario u          ON u.id_usuario = p.id_usuario
    LEFT JOIN pedido_producto pp ON pp.id_pedido = p.id_pedido  -- LEFT: incluye pedidos sin líneas
    WHERE DATE(p.fecha) = p_fecha                               -- fecha es DATETIME
    GROUP BY p.id_pedido, u.nombre, p.fecha, p.estado
    ORDER BY p.fecha;
END
