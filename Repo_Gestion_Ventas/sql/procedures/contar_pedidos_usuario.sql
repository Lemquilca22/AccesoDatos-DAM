-- Devuelve en un parámetro OUT cuántos pedidos ha hecho un usuario.
-- Ojo: sin DELIMITER. Este fichero se manda entero desde Java como UNA sola sentencia.
CREATE PROCEDURE contar_pedidos_usuario(
    IN  p_id_usuario INT,
    OUT p_num_pedidos INT
)
BEGIN
    SELECT COUNT(*) INTO p_num_pedidos
    FROM pedido
    WHERE id_usuario = p_id_usuario;
END
