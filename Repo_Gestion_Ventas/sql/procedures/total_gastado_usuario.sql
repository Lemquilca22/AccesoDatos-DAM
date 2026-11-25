-- Devuelve en un parámetro OUT el importe total gastado por un usuario en todos sus pedidos.
-- La tabla pedido no guarda el total: se suma cantidad * precio_unitario de sus líneas.
-- Ojo: sin DELIMITER. Este fichero se manda entero desde Java como UNA sola sentencia.
CREATE PROCEDURE total_gastado_usuario(
    IN  p_id_usuario INT,
    OUT p_total      DECIMAL(10,2)
)
BEGIN
    -- COALESCE evita que devuelva NULL si el usuario no tiene pedidos
    SELECT COALESCE(SUM(pp.cantidad * pp.precio_unitario), 0) INTO p_total
    FROM pedido p
    JOIN pedido_producto pp ON pp.id_pedido = p.id_pedido
    WHERE p.id_usuario = p_id_usuario;
END
