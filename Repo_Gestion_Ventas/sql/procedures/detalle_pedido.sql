-- Devuelve las líneas de un pedido con el nombre del producto y el subtotal de cada línea.
CREATE PROCEDURE detalle_pedido(IN p_id_pedido INT)
BEGIN
    SELECT pp.id_producto,
           pr.nombre_producto,
           pp.cantidad,
           pp.precio_unitario,
           pp.cantidad * pp.precio_unitario AS subtotal
    FROM pedido_producto pp
    JOIN producto pr ON pr.id_producto = pp.id_producto
    WHERE pp.id_pedido = p_id_pedido
    ORDER BY pr.nombre_producto;
END
