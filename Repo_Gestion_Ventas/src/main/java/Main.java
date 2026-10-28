import dao.AuditLogger;
import dao.DirectoryManager;
import dao.ProductoDao;
import dao.UsuarioDao;
import dao.PedidoDao;
import dao.DetallePedidoDao;
import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            System.out.println("=== [FASE 2] GESTIÓN Y CREACIÓN DE DIRECTORIOS ===");
            DirectoryManager.iniciarEstructura();
            DirectoryManager.inspeccionarDirectorio("data");

            System.out.println("\n=== [FASE 3] FLUIDOS DE TEXTO Y AUDITORÍA ===");
            AuditLogger.registrarLog("INFO", "Sistema de Gestión de Ventas inicializado con éxito.");
            AuditLogger.registrarLog("SUCCESS", "Directorios de productos, usuarios y pedidos verificados.");
            AuditLogger.registrarLog("WARNING", "Comprobando integridad del almacenamiento local.");

            AuditLogger.buscarPalabraClave("INFO");

            System.out.println("\n=== [FASE 4] PERSISTENCIA BINARIA Y DAOs ===");

            // 1. Probando UsuarioDao
            List<Usuario> usuarios = new ArrayList<>();
            usuarios.add(new Usuario(1, "Ana Gómez", "ana@email.com", "600111222", "Calle Mayor 1", "pass123", Rol.ADMINISTRADOR));
            UsuarioDao.guardarUsuarios(usuarios);
            List<Usuario> usuariosCargados = UsuarioDao.cargarUsuarios();
            System.out.println("👤 Usuarios recuperados del disco: " + usuariosCargados.size());

            // 2. Probando ProductoDao
            List<Producto> productos = new ArrayList<>();
            productos.add(new Producto(1, "Laptop Gamer", 1200.50, 10, "Tecnología"));
            productos.add(new Producto(2, "Teclado Mecánico", 75.99, 25, "Accesorios"));
            ProductoDao.guardarInventario(productos);
            List<Producto> productosCargados = ProductoDao.cargarInventario();
            System.out.println("📦 Productos recuperados del disco: " + productosCargados.size());

            // 3. Probando PedidoDao
            List<Pedido> pedidos = new ArrayList<>();
            pedidos.add(new Pedido(1, 1, LocalDateTime.now(), EstadoPedido.PENDIENTE));
            PedidoDao.guardarPedidos(pedidos);
            List<Pedido> pedidosCargados = PedidoDao.cargarPedidos();
            System.out.println("🛒 Pedidos recuperados del disco: " + pedidosCargados.size());

            // 4. Probando DetallePedidoDao
            List<DetallePedido> detalles = new ArrayList<>();
            detalles.add(new DetallePedido(1, 1, 2, 1200.50));
            DetallePedidoDao.guardarDetalles(detalles);
            List<DetallePedido> detallesCargados = DetallePedidoDao.cargarDetalles();
            System.out.println("📄 Detalles recuperados del disco: " + detallesCargados.size());

            System.out.println("\n✨ ¡Todas las fases ejecutadas y validadas correctamente!");

        } catch (Exception e) {
            System.err.println("❌ Error en la ejecución del sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}