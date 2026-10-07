import dao.PedidoDAO;
import dao.ProductoDAO;
import dao.UsuarioDAO;
import model.EstadoPedido;
import model.Pedido;
import model.PedidoProducto;
import model.Producto;
import model.Usuario;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static final ProductoDAO productoDAO = new ProductoDAO();
    private static final PedidoDAO pedidoDAO = new PedidoDAO();

    public static void main(String[] args) {
        cargarDatosDePrueba();

        int opcion;
        do {
            System.out.println("\n===== GESTIÓN DE VENTAS - TIENDA DE BEBIDAS =====");
            System.out.println("1. Usuarios");
            System.out.println("2. Productos");
            System.out.println("3. Pedidos");
            System.out.println("4. Informes de ventas");
            System.out.println("0. Salir");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1 -> menuUsuarios();
                case 2 -> menuProductos();
                case 3 -> menuPedidos();
                case 4 -> menuInformes();
                case 0 -> System.out.println("¡Hasta pronto!");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }


    private static void menuUsuarios() {
        int opcion;
        do {
            System.out.println("\n--- USUARIOS ---");
            System.out.println("1. Listar usuarios");
            System.out.println("2. Buscar usuario por nombre");
            System.out.println("3. Alta de usuario");
            System.out.println("4. Modificar usuario");
            System.out.println("5. Baja de usuario");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1 -> mostrarUsuarios(usuarioDAO.obtenerTodos());
                case 2 -> mostrarUsuarios(usuarioDAO.buscarPorNombre(leerTexto("Nombre a buscar: ")));
                case 3 -> altaUsuario();
                case 4 -> modificarUsuario();
                case 5 -> bajaUsuario();
                case 0 -> {}
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private static void mostrarUsuarios(List<Usuario> usuarios) {
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios.");
            return;
        }
        System.out.printf("%-4s %-22s %-28s %-12s %s%n", "ID", "NOMBRE", "EMAIL", "TELÉFONO", "DIRECCIÓN");
        for (Usuario u : usuarios) {
            System.out.printf("%-4d %-22s %-28s %-12s %s%n",
                    u.getId_usuario(), u.getNombre(), u.getEmail(), u.getTelefono(), u.getDireccion());
        }
    }

    private static void altaUsuario() {
        String nombre = leerTexto("Nombre: ");
        String email = leerEmail("Email: ");
        if (usuarioDAO.obtenerPorEmail(email) != null) {
            System.out.println("Ya existe un usuario con ese email.");
            return;
        }
        String telefono = leerTexto("Teléfono: ");
        String direccion = leerTexto("Dirección: ");
        Usuario u = usuarioDAO.insertar(new Usuario(0, nombre, email, telefono, direccion));
        System.out.println("Usuario creado con ID " + u.getId_usuario());
    }

    private static void modificarUsuario() {
        Usuario u = pedirUsuario();
        if (u == null) return;
        System.out.println("(Deja en blanco para mantener el valor actual)");
        String nombre = leerTextoOpcional("Nombre [" + u.getNombre() + "]: ");
        String email = leerTextoOpcional("Email [" + u.getEmail() + "]: ");
        String telefono = leerTextoOpcional("Teléfono [" + u.getTelefono() + "]: ");
        String direccion = leerTextoOpcional("Dirección [" + u.getDireccion() + "]: ");

        if (!email.isEmpty()) {
            if (!esEmailValido(email)) {
                System.out.println("Email no válido. No se ha modificado el usuario.");
                return;
            }
            Usuario otro = usuarioDAO.obtenerPorEmail(email);
            if (otro != null && otro.getId_usuario() != u.getId_usuario()) {
                System.out.println("Ese email ya pertenece a otro usuario. No se ha modificado.");
                return;
            }
            u.setEmail(email);
        }
        if (!nombre.isEmpty()) u.setNombre(nombre);
        if (!telefono.isEmpty()) u.setTelefono(telefono);
        if (!direccion.isEmpty()) u.setDireccion(direccion);

        usuarioDAO.actualizar(u);
        System.out.println("Usuario actualizado.");
    }

    private static void bajaUsuario() {
        Usuario u = pedirUsuario();
        if (u == null) return;
        if (!pedidoDAO.obtenerPorUsuario(u.getId_usuario()).isEmpty()) {
            System.out.println("No se puede eliminar: el usuario tiene pedidos registrados.");
            return;
        }
        if (leerSiNo("¿Eliminar a " + u.getNombre() + "? (s/n): ")) {
            usuarioDAO.eliminar(u.getId_usuario());
            System.out.println("Usuario eliminado.");
        }
    }

    private static Usuario pedirUsuario() {
        int id = leerEntero("ID del usuario: ");
        Usuario u = usuarioDAO.obtenerPorId(id);
        if (u == null) {
            System.out.println("No existe ningún usuario con ID " + id);
        }
        return u;
    }


    private static void menuProductos() {
        int opcion;
        do {
            System.out.println("\n--- PRODUCTOS ---");
            System.out.println("1. Listar todos los productos");
            System.out.println("2. Listar productos disponibles");
            System.out.println("3. Buscar producto por nombre");
            System.out.println("4. Alta de producto");
            System.out.println("5. Modificar producto");
            System.out.println("6. Cambiar disponibilidad");
            System.out.println("7. Baja de producto");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1 -> mostrarProductos(productoDAO.obtenerTodos());
                case 2 -> mostrarProductos(productoDAO.obtenerDisponibles());
                case 3 -> mostrarProductos(productoDAO.buscarPorNombre(leerTexto("Nombre a buscar: ")));
                case 4 -> altaProducto();
                case 5 -> modificarProducto();
                case 6 -> cambiarDisponibilidad();
                case 7 -> bajaProducto();
                case 0 -> {}
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private static void mostrarProductos(List<Producto> productos) {
        if (productos.isEmpty()) {
            System.out.println("No hay productos.");
            return;
        }
        System.out.printf("%-4s %-30s %10s  %s%n", "ID", "PRODUCTO", "PRECIO", "DISPONIBLE");
        for (Producto p : productos) {
            System.out.printf("%-4d %-30s %10s  %s%n",
                    p.getIdProducto(), p.getNombreProducto(), euros(p.getPrecio()), p.isDisponible() ? "Sí" : "No");
        }
    }

    private static void altaProducto() {
        String nombre = leerTexto("Nombre del producto: ");
        BigDecimal precio = leerPrecio("Precio (€): ");
        boolean disponible = leerSiNo("¿Disponible para la venta? (s/n): ");
        Producto p = productoDAO.insertar(new Producto(nombre, precio, disponible));
        System.out.println("Producto creado con ID " + p.getIdProducto());
    }

    private static void modificarProducto() {
        Producto p = pedirProducto();
        if (p == null) return;
        System.out.println("(Deja en blanco para mantener el valor actual)");
        String nombre = leerTextoOpcional("Nombre [" + p.getNombreProducto() + "]: ");
        String precioTexto = leerTextoOpcional("Precio [" + p.getPrecio() + "]: ");

        if (!precioTexto.isEmpty()) {
            BigDecimal precio = parsearPrecio(precioTexto);
            if (precio == null) {
                System.out.println("Precio no válido. No se ha modificado el producto.");
                return;
            }
            p.setPrecio(precio);
        }
        if (!nombre.isEmpty()) p.setNombreProducto(nombre);

        productoDAO.actualizar(p);
        System.out.println("Producto actualizado.");
    }

    private static void cambiarDisponibilidad() {
        Producto p = pedirProducto();
        if (p == null) return;
        p.setDisponible(!p.isDisponible());
        productoDAO.actualizar(p);
        System.out.println(p.getNombreProducto() + " ahora está " + (p.isDisponible() ? "DISPONIBLE" : "NO DISPONIBLE"));
    }

    private static void bajaProducto() {
        Producto p = pedirProducto();
        if (p == null) return;
        if (pedidoDAO.productoEnUso(p.getIdProducto())) {
            System.out.println("No se puede eliminar: el producto aparece en pedidos. Puedes marcarlo como no disponible.");
            return;
        }
        if (leerSiNo("¿Eliminar " + p.getNombreProducto() + "? (s/n): ")) {
            productoDAO.eliminar(p.getIdProducto());
            System.out.println("Producto eliminado.");
        }
    }

    private static Producto pedirProducto() {
        int id = leerEntero("ID del producto: ");
        Producto p = productoDAO.obtenerPorId(id);
        if (p == null) {
            System.out.println("No existe ningún producto con ID " + id);
        }
        return p;
    }


    private static void menuPedidos() {
        int opcion;
        do {
            System.out.println("\n--- PEDIDOS ---");
            System.out.println("1. Nuevo pedido");
            System.out.println("2. Listar pedidos");
            System.out.println("3. Ver detalle de un pedido");
            System.out.println("4. Pedidos de un usuario");
            System.out.println("5. Cambiar estado de un pedido");
            System.out.println("6. Eliminar pedido");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1 -> nuevoPedido();
                case 2 -> mostrarPedidos(pedidoDAO.obtenerTodos());
                case 3 -> verDetallePedido();
                case 4 -> {
                    Usuario u = pedirUsuario();
                    if (u != null) mostrarPedidos(pedidoDAO.obtenerPorUsuario(u.getId_usuario()));
                }
                case 5 -> cambiarEstadoPedido();
                case 6 -> eliminarPedido();
                case 0 -> {}
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private static void nuevoPedido() {
        Usuario u = pedirUsuario();
        if (u == null) return;

        List<Producto> disponibles = productoDAO.obtenerDisponibles();
        if (disponibles.isEmpty()) {
            System.out.println("No hay productos disponibles para vender.");
            return;
        }

        Pedido pedido = new Pedido(u.getId_usuario());
        System.out.println("\nProductos disponibles:");
        mostrarProductos(disponibles);

        while (true) {
            int idProducto = leerEntero("\nID del producto a añadir (0 para terminar): ");
            if (idProducto == 0) break;

            Producto p = productoDAO.obtenerPorId(idProducto);
            if (p == null) {
                System.out.println("No existe ese producto.");
                continue;
            }
            if (!p.isDisponible()) {
                System.out.println("Ese producto no está disponible.");
                continue;
            }
            int cantidad = leerEnteroPositivo("Cantidad: ");
            pedido.addLinea(new PedidoProducto(p.getIdProducto(), cantidad, p.getPrecio()));
            System.out.println("Añadido: " + cantidad + " x " + p.getNombreProducto()
                    + "  | Total actual: " + euros(pedido.getTotal()));
        }

        if (pedido.getLineas().isEmpty()) {
            System.out.println("Pedido cancelado: no se ha añadido ningún producto.");
            return;
        }

        imprimirDetalle(pedido);
        if (leerSiNo("¿Confirmar pedido? (s/n): ")) {
            pedidoDAO.insertar(pedido);
            System.out.println("Pedido registrado con ID " + pedido.getIdPedido());
        } else {
            System.out.println("Pedido descartado.");
        }
    }

    private static void mostrarPedidos(List<Pedido> pedidos) {
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos.");
            return;
        }
        System.out.printf("%-5s %-17s %-22s %-10s %7s %12s%n", "ID", "FECHA", "CLIENTE", "ESTADO", "LÍNEAS", "TOTAL");
        for (Pedido p : pedidos) {
            System.out.printf("%-5d %-17s %-22s %-10s %7d %12s%n",
                    p.getIdPedido(), p.getFecha().format(FORMATO_FECHA), nombreCliente(p.getIdUsuario()),
                    p.getEstado(), p.getLineas().size(), euros(p.getTotal()));
        }
    }

    private static void verDetallePedido() {
        Pedido p = pedirPedido();
        if (p != null) imprimirDetalle(p);
    }

    private static void imprimirDetalle(Pedido pedido) {
        System.out.println("\n----------------------------------------------------------");
        if (pedido.getIdPedido() > 0) System.out.println("Pedido nº " + pedido.getIdPedido());
        System.out.println("Cliente: " + nombreCliente(pedido.getIdUsuario()));
        System.out.println("Fecha:   " + pedido.getFecha().format(FORMATO_FECHA));
        System.out.println("Estado:  " + pedido.getEstado());
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-28s %6s %10s %11s%n", "PRODUCTO", "CANT.", "P. UNIT.", "SUBTOTAL");
        for (PedidoProducto linea : pedido.getLineas()) {
            System.out.printf("%-28s %6d %10s %11s%n",
                    nombreProducto(linea.getIdProducto()), linea.getCantidad(),
                    euros(linea.getPrecioUnitario()), euros(linea.getSubtotal()));
        }
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-28s %29s%n", "TOTAL", euros(pedido.getTotal()));
    }

    private static void cambiarEstadoPedido() {
        Pedido p = pedirPedido();
        if (p == null) return;
        System.out.println("Estado actual: " + p.getEstado());
        EstadoPedido[] estados = EstadoPedido.values();
        for (int i = 0; i < estados.length; i++) {
            System.out.println((i + 1) + ". " + estados[i]);
        }
        int opcion = leerEntero("Nuevo estado: ");
        if (opcion < 1 || opcion > estados.length) {
            System.out.println("Estado no válido.");
            return;
        }
        pedidoDAO.actualizarEstado(p.getIdPedido(), estados[opcion - 1]);
        System.out.println("Estado actualizado a " + estados[opcion - 1]);
    }

    private static void eliminarPedido() {
        Pedido p = pedirPedido();
        if (p == null) return;
        if (leerSiNo("¿Eliminar el pedido " + p.getIdPedido() + " (" + euros(p.getTotal()) + ")? (s/n): ")) {
            pedidoDAO.eliminar(p.getIdPedido());
            System.out.println("Pedido eliminado.");
        }
    }

    private static Pedido pedirPedido() {
        int id = leerEntero("ID del pedido: ");
        Pedido p = pedidoDAO.obtenerPorId(id);
        if (p == null) {
            System.out.println("No existe ningún pedido con ID " + id);
        }
        return p;
    }


    private static void menuInformes() {
        int opcion;
        do {
            System.out.println("\n--- INFORMES DE VENTAS ---");
            System.out.println("1. Resumen general");
            System.out.println("2. Productos más vendidos");
            System.out.println("3. Ventas por cliente");
            System.out.println("4. Pedidos por estado");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1 -> informeResumen();
                case 2 -> informeProductosMasVendidos();
                case 3 -> informeVentasPorCliente();
                case 4 -> informePedidosPorEstado();
                case 0 -> {}
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private static List<Pedido> pedidosValidos() {
        List<Pedido> validos = new ArrayList<>();
        for (Pedido p : pedidoDAO.obtenerTodos()) {
            if (p.getEstado() != EstadoPedido.CANCELADO) {
                validos.add(p);
            }
        }
        return validos;
    }

    private static void informeResumen() {
        List<Pedido> pedidos = pedidosValidos();
        BigDecimal facturado = BigDecimal.ZERO;
        int unidades = 0;
        for (Pedido p : pedidos) {
            facturado = facturado.add(p.getTotal());
            for (PedidoProducto l : p.getLineas()) {
                unidades += l.getCantidad();
            }
        }
        BigDecimal ticketMedio = pedidos.isEmpty()
                ? BigDecimal.ZERO
                : facturado.divide(BigDecimal.valueOf(pedidos.size()), 2, RoundingMode.HALF_UP);

        System.out.println("\nPedidos (sin cancelados): " + pedidos.size());
        System.out.println("Unidades vendidas:        " + unidades);
        System.out.println("Total facturado:          " + euros(facturado));
        System.out.println("Ticket medio:             " + euros(ticketMedio));
        System.out.println("Clientes registrados:     " + usuarioDAO.obtenerTodos().size());
        System.out.println("Productos en catálogo:    " + productoDAO.obtenerTodos().size()
                + " (" + productoDAO.obtenerDisponibles().size() + " disponibles)");
    }

    private static void informeProductosMasVendidos() {
        Map<Integer, Integer> unidades = new LinkedHashMap<>();
        Map<Integer, BigDecimal> importes = new LinkedHashMap<>();
        for (Pedido p : pedidosValidos()) {
            for (PedidoProducto l : p.getLineas()) {
                unidades.merge(l.getIdProducto(), l.getCantidad(), Integer::sum);
                importes.merge(l.getIdProducto(), l.getSubtotal(), BigDecimal::add);
            }
        }
        if (unidades.isEmpty()) {
            System.out.println("Todavía no hay ventas.");
            return;
        }
        List<Integer> ids = new ArrayList<>(unidades.keySet());
        ids.sort((a, b) -> unidades.get(b) - unidades.get(a));

        System.out.printf("%-4s %-30s %8s %12s%n", "#", "PRODUCTO", "UNIDADES", "IMPORTE");
        int posicion = 1;
        for (int id : ids) {
            System.out.printf("%-4d %-30s %8d %12s%n",
                    posicion++, nombreProducto(id), unidades.get(id), euros(importes.get(id)));
        }
    }

    private static void informeVentasPorCliente() {
        Map<Integer, Integer> numPedidos = new LinkedHashMap<>();
        Map<Integer, BigDecimal> gastado = new LinkedHashMap<>();
        for (Pedido p : pedidosValidos()) {
            numPedidos.merge(p.getIdUsuario(), 1, Integer::sum);
            gastado.merge(p.getIdUsuario(), p.getTotal(), BigDecimal::add);
        }
        if (gastado.isEmpty()) {
            System.out.println("Todavía no hay ventas.");
            return;
        }
        List<Integer> ids = new ArrayList<>(gastado.keySet());
        ids.sort((a, b) -> gastado.get(b).compareTo(gastado.get(a)));

        System.out.printf("%-4s %-25s %8s %12s%n", "ID", "CLIENTE", "PEDIDOS", "GASTADO");
        for (int id : ids) {
            System.out.printf("%-4d %-25s %8d %12s%n", id, nombreCliente(id), numPedidos.get(id), euros(gastado.get(id)));
        }
    }

    private static void informePedidosPorEstado() {
        Map<EstadoPedido, Integer> conteo = new LinkedHashMap<>();
        for (EstadoPedido e : EstadoPedido.values()) {
            conteo.put(e, 0);
        }
        for (Pedido p : pedidoDAO.obtenerTodos()) {
            conteo.merge(p.getEstado(), 1, Integer::sum);
        }
        for (Map.Entry<EstadoPedido, Integer> e : conteo.entrySet()) {
            System.out.printf("%-10s %d%n", e.getKey(), e.getValue());
        }
    }


    private static String nombreCliente(int idUsuario) {
        Usuario u = usuarioDAO.obtenerPorId(idUsuario);
        return u != null ? u.getNombre() : "(usuario " + idUsuario + ")";
    }

    private static String nombreProducto(int idProducto) {
        Producto p = productoDAO.obtenerPorId(idProducto);
        return p != null ? p.getNombreProducto() : "(producto " + idProducto + ")";
    }

    private static String euros(BigDecimal cantidad) {
        return cantidad.setScale(2, RoundingMode.HALF_UP) + " €";
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número entero.");
            }
        }
    }

    private static int leerEnteroPositivo(String mensaje) {
        while (true) {
            int n = leerEntero(mensaje);
            if (n > 0) return n;
            System.out.println("Debe ser mayor que 0.");
        }
    }

    private static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine().trim();
            if (!linea.isEmpty()) return linea;
            System.out.println("Este campo no puede estar vacío.");
        }
    }

    private static String leerTextoOpcional(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    private static String leerEmail(String mensaje) {
        while (true) {
            String email = leerTexto(mensaje);
            if (esEmailValido(email)) return email;
            System.out.println("Email no válido.");
        }
    }

    private static boolean esEmailValido(String email) {
        return email.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    }

    private static BigDecimal leerPrecio(String mensaje) {
        while (true) {
            BigDecimal precio = parsearPrecio(leerTexto(mensaje));
            if (precio != null) return precio;
            System.out.println("Precio no válido (ejemplo: 2.50).");
        }
    }

    private static BigDecimal parsearPrecio(String texto) {
        try {
            BigDecimal precio = new BigDecimal(texto.replace(',', '.'));
            if (precio.signum() <= 0) return null;
            return precio.setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String r = sc.nextLine().trim().toLowerCase();
            if (r.equals("s") || r.equals("si") || r.equals("sí")) return true;
            if (r.equals("n") || r.equals("no")) return false;
            System.out.println("Responde s o n.");
        }
    }


    private static void cargarDatosDePrueba() {
        Usuario ana = usuarioDAO.insertar(new Usuario(0, "Ana García", "ana@correo.com", "600111222", "C/ Mayor 1, Madrid"));
        Usuario luis = usuarioDAO.insertar(new Usuario(0, "Luis Pérez", "luis@correo.com", "600333444", "Av. del Puerto 22, Valencia"));
        usuarioDAO.insertar(new Usuario(0, "Marta López", "marta@correo.com", "600555666", "C/ Sierpes 5, Sevilla"));

        Producto agua = productoDAO.insertar(new Producto("Agua mineral 1.5L", new BigDecimal("0.60"), true));
        Producto cola = productoDAO.insertar(new Producto("Refresco de cola 2L", new BigDecimal("1.85"), true));
        Producto zumo = productoDAO.insertar(new Producto("Zumo de naranja 1L", new BigDecimal("1.40"), true));
        Producto cerveza = productoDAO.insertar(new Producto("Cerveza pack 6 x 33cl", new BigDecimal("4.20"), true));
        productoDAO.insertar(new Producto("Vino tinto Rioja 75cl", new BigDecimal("7.95"), true));
        productoDAO.insertar(new Producto("Bebida energética 50cl", new BigDecimal("1.65"), false));

        Pedido p1 = new Pedido(ana.getId_usuario());
        p1.addLinea(new PedidoProducto(agua.getIdProducto(), 6, agua.getPrecio()));
        p1.addLinea(new PedidoProducto(zumo.getIdProducto(), 2, zumo.getPrecio()));
        p1.setEstado(EstadoPedido.ENTREGADO);
        pedidoDAO.insertar(p1);

        Pedido p2 = new Pedido(luis.getId_usuario());
        p2.addLinea(new PedidoProducto(cerveza.getIdProducto(), 2, cerveza.getPrecio()));
        p2.addLinea(new PedidoProducto(cola.getIdProducto(), 1, cola.getPrecio()));
        pedidoDAO.insertar(p2);
    }
}
