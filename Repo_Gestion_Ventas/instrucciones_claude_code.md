# Tarea: añadir procedimientos almacenados de consulta al proyecto

Crea en este proyecto los archivos de abajo **con el contenido exacto** (no lo modifiques ni lo "mejores").

## Reglas
- Crea la carpeta `sql/procedures/` en la raíz del proyecto (al lado de `sql/01_esquema.sql`, que NO debes tocar).
- Crea `ScriptLoader.java` en `src/main/java/dao/`.
- Los `.sql` NO llevan `DELIMITER`: cada fichero es una sola sentencia que Java envía entera. No añadas `DELIMITER` ni `;` después de `END`.
- No toques ningún otro archivo del proyecto.
- Al terminar, busca en el proyecto la clase que abre la conexión JDBC (DriverManager.getConnection) y dime cuál es y qué URL/usuario usa, para adaptar ScriptLoader. No cambies nada todavía: solo infórmame.

## `sql/procedures/sp_detalle_pedido.sql`

```sql
-- Devuelve las líneas de un pedido con el nombre del producto y el subtotal de cada línea.
CREATE PROCEDURE sp_detalle_pedido(IN p_id_pedido INT)
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
```

## `sql/procedures/sp_pedidos_por_estado.sql`

```sql
-- Devuelve los pedidos que están en un estado concreto (PENDIENTE, ENTREGADO...).
CREATE PROCEDURE sp_pedidos_por_estado(IN p_estado VARCHAR(50))
BEGIN
    SELECT p.id_pedido,
           u.nombre,
           p.fecha,
           p.estado
    FROM pedido p
    JOIN usuario u ON u.id_usuario = p.id_usuario
    WHERE p.estado = p_estado
    ORDER BY p.fecha;
END
```

## `sql/procedures/sp_pedidos_por_fecha.sql`

```sql
-- Devuelve (como ResultSet, sin OUT) los pedidos de un día concreto
-- con el nombre del usuario y el total de cada pedido.
CREATE PROCEDURE sp_pedidos_por_fecha(IN p_fecha DATE)
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
```

## `sql/procedures/sp_pedidos_usuario.sql`

```sql
-- Devuelve el historial de pedidos de un usuario con el total de cada pedido.
CREATE PROCEDURE sp_pedidos_usuario(IN p_id_usuario INT)
BEGIN
    SELECT p.id_pedido,
           p.fecha,
           p.estado,
           COALESCE(SUM(pp.cantidad * pp.precio_unitario), 0) AS total
    FROM pedido p
    LEFT JOIN pedido_producto pp ON pp.id_pedido = p.id_pedido  -- LEFT: incluye pedidos sin líneas
    WHERE p.id_usuario = p_id_usuario
    GROUP BY p.id_pedido, p.fecha, p.estado
    ORDER BY p.fecha DESC;
END
```

## `sql/procedures/sp_productos_disponibles.sql`

```sql
-- Devuelve el catálogo de productos que se pueden pedir (disponible = 1). Sin parámetros.
CREATE PROCEDURE sp_productos_disponibles()
BEGIN
    SELECT id_producto,
           nombre_producto,
           precio
    FROM producto
    WHERE disponible = 1
    ORDER BY nombre_producto;
END
```

## `sql/procedures/sp_total_pedido.sql`

```sql
-- MODELO COMPLETO (equivalente a contar_por_especie del enunciado)
-- Devuelve en un parámetro OUT el importe total de un pedido.
-- Ojo: sin DELIMITER. Este fichero se manda entero desde Java como UNA sola sentencia.
CREATE PROCEDURE sp_total_pedido(
    IN  p_id_pedido INT,
    OUT p_total     DECIMAL(10,2)
)
BEGIN
    -- COALESCE evita que devuelva NULL si el pedido no tiene líneas
    SELECT COALESCE(SUM(cantidad * precio_unitario), 0) INTO p_total
    FROM pedido_producto
    WHERE id_pedido = p_id_pedido;
END
```

## `src/main/java/dao/ScriptLoader.java`

```java
package dao;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Crea (o recrea) en la BD todos los procedimientos de la carpeta sql/procedures.
 * Regla: cada fichero contiene UN solo CREATE PROCEDURE, sin DELIMITER,
 * y el nombre del fichero coincide con el del procedimiento (sp_x.sql -> sp_x).
 */
public class ScriptLoader {

    private static final String URL =
        "jdbc:mysql://localhost:3306/gestion_ventas?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    private static final Path CARPETA = Paths.get("sql", "procedures");

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USUARIO, CLAVE)) {
            cargarProcedimientos(conn);
        } catch (SQLException | IOException e) {
            System.err.println("Error cargando procedimientos: " + e.getMessage());
        }
    }

    public static void cargarProcedimientos(Connection conn) throws IOException, SQLException {
        List<Path> scripts;
        try (Stream<Path> s = Files.list(CARPETA)) {
            scripts = s.filter(p -> p.toString().endsWith(".sql"))
                       .sorted()
                       .collect(Collectors.toList());
        }

        try (Statement st = conn.createStatement()) {
            for (Path script : scripts) {
                String nombre = script.getFileName().toString().replace(".sql", "");
                String sql = Files.readString(script, StandardCharsets.UTF_8);

                st.execute("DROP PROCEDURE IF EXISTS " + nombre); // así se puede relanzar
                st.execute(sql);                                   // el fichero entero = 1 sentencia
                System.out.println("Procedimiento creado: " + nombre);
            }
        }
    }
}
```

