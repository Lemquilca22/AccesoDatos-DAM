package app;

import dao.AccesoDirectoProductos;
import dao.AuditLogger;
import dao.DirectoryManager;
import dao.ProductoDao;
import model.Producto;
import util.Consola;
import util.DatosPrueba;
import util.ExportadorCSV;

import java.io.IOException;
import java.util.List;

/**
 * Menú de consola de la aplicación (Fase 6).
 * <p>
 * Notas para entender el codigo:
 * esta clase solo muestra el menú y llama a los métodos de las otras clases. No abre ningún fichero ni
 * lee del teclado directamente: los ficheros los gestionan los DAO ({@code dao}), el teclado lo lee
 * {@link Consola} y los datos de ejemplo salen de {@link DatosPrueba} ({@code util}).
 */
public class MainApp {

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("  DIGITECH - Gestión de ventas");
        System.out.println("==============================================");

        //Antes de nada creamos ./data y sus subcarpetas, si no el resto de opciones fallarían
        titulo("[FASE 2] GESTIÓN Y CREACIÓN DE DIRECTORIOS");
        try {
            DirectoryManager.iniciarEstructura();
        } catch (IOException e) {
            System.err.println("Error en la ejecución del sistema: " + e.getMessage());
            System.err.println("No se han creado las carpetas, vuelva a intentarlo");
            return;
        }
        AuditLogger.registrarLog("INFO", "Aplicación iniciada desde MainApp.");

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = Consola.leerEntero("Elige una opción: ");

            //Usamos el switch "nuevo" con flechas (->). La diferencia con el clásico es que no hace falta poner
            //break en cada case: solo se ejecuta el case que coincide.
            switch (opcion) {
                case 1 -> {
                    titulo("[FASE 2] GESTIÓN Y CREACIÓN DE DIRECTORIOS");
                    DirectoryManager.inspeccionarDirectorio("data");
                }
                case 2 -> registrarEvento();
                case 3 -> buscarEnLog();
                case 4 -> listarInventario();
                case 5 -> cargarDatosDePrueba();
                case 6 -> generarFicheroAccesoDirecto();
                case 7 -> leerProductoDirecto();
                case 8 -> modificarStockDirecto();
                case 9 -> exportarCSV();
                case 0 -> salir = true;
                default -> System.err.println("Opción no válida, intentelo de nuevo");
            }
        }

        AuditLogger.registrarLog("INFO", "Aplicación cerrada por el usuario.");
        System.out.println("Funciona, SIUUUUUUUUUUUUUU");
    }

    /**
     * Imprime un título de sección con el mismo formato que usa {@code Main}:
     * {@code === [FASE X] TEXTO ===}, dejando una línea en blanco antes para que se lea mejor.
     */
    private static void titulo(String texto) {
        System.out.println("\n=== " + texto + " ===");
    }

    /** Imprime las opciones del menú, agrupadas por fases igual que en {@code Main}. */
    private static void mostrarMenu() {
        titulo("MENÚ PRINCIPAL");
        System.out.println("[FASE 2] GESTIÓN Y CREACIÓN DE DIRECTORIOS");
        System.out.println("   1. Inspeccionar el directorio ./data");
        System.out.println("[FASE 3] FLUIDOS DE TEXTO Y AUDITORÍA");
        System.out.println("   2. Registrar un evento en el log");
        System.out.println("   3. Buscar una palabra en el log");
        System.out.println("[FASE 4] SERIALIZACIÓN DE INVENTARIO");
        System.out.println("   4. Listar inventario");
        System.out.println("   5. Cargar datos de prueba (sobrescribe el inventario)");
        System.out.println("[FASE 5] ACCESO ALEATORIO DIRECTO");
        System.out.println("   6. Generar el fichero de acceso directo desde el inventario");
        System.out.println("   7. Leer un producto por posición");
        System.out.println("   8. Modificar el stock de un producto por posición");
        System.out.println("[FASE 6] EXPORTACIÓN A CSV");
        System.out.println("   9. Exportar inventario a CSV");
        System.out.println("   0. Salir");
    }

    // ----------------------------------------------------------------------------------------------
    // Opciones del menú
    // ----------------------------------------------------------------------------------------------

    /** Opción 2: pide un nivel y una descripción y lo escribe en el audit.log. */
    private static void registrarEvento() {
        titulo("[FASE 3] FLUIDOS DE TEXTO Y AUDITORÍA");
        String nivel = Consola.leerTexto("Nivel (INFO, SUCCESS, WARNING, ERROR): ").toUpperCase();
        String descripcion = Consola.leerTexto("Descripción del evento: ");
        AuditLogger.registrarLog(nivel, descripcion);
    }

    /** Opción 3: busca una palabra en el log usando el BufferedReader del AuditLogger. */
    private static void buscarEnLog() {
        titulo("[FASE 3] FLUIDOS DE TEXTO Y AUDITORÍA");
        AuditLogger.buscarPalabraClave(Consola.leerTexto("Palabra a buscar: "));
    }

    /** Opción 4: deserializa inventario.dat y muestra los productos. */
    private static void listarInventario() {
        titulo("[FASE 4] SERIALIZACIÓN DE INVENTARIO");
        List<Producto> inventario = ProductoDao.cargarInventario();
        if (inventario.isEmpty()) {
            System.out.println("El inventario está vacío. Prueba con la opción 5.");
            return;
        }
        inventario.forEach(System.out::println);
    }

    /** Opción 5: rellena el inventario con los productos de {@link DatosPrueba}. */
    private static void cargarDatosDePrueba() {
        titulo("[FASE 4] SERIALIZACIÓN DE INVENTARIO");
        List<Producto> prueba = DatosPrueba.productos();
        ProductoDao.guardarInventario(prueba);
        System.out.println("Cargados " + prueba.size() + " productos de prueba en inventario.dat");
    }

    /** Opción 6: vuelca el inventario serializado al fichero de registros de tamaño fijo. */
    private static void generarFicheroAccesoDirecto() {
        titulo("[FASE 5] ACCESO ALEATORIO DIRECTO");
        List<Producto> inventario = ProductoDao.cargarInventario();
        if (inventario.isEmpty()) {
            System.out.println("No hay productos en el inventario que volcar.");
            return;
        }
        AccesoDirectoProductos.guardarTodos(inventario);
        System.out.println("Registros en el fichero: " + AccesoDirectoProductos.numeroRegistros());

        System.out.println("Contenido completo del fichero de acceso directo:");
        AccesoDirectoProductos.leerTodos().forEach(System.out::println);
    }

    /** Opción 7: lee un único registro con seek() sin leer el resto del fichero. */
    private static void leerProductoDirecto() {
        titulo("[FASE 5] ACCESO ALEATORIO DIRECTO");
        int posicion = pedirPosicion();
        if (posicion < 0) {
            return;
        }
        Producto p = AccesoDirectoProductos.leerProducto(posicion);
        if (p != null) {
            System.out.println("Registro " + posicion + ": " + p);
            System.out.println("(La categoría sale como 'General' porque el registro de tamaño fijo no la guarda)");
        }
    }

    /** Opción 8: cambia solo los 4 bytes del stock de un registro concreto. */
    private static void modificarStockDirecto() {
        titulo("[FASE 5] ACCESO ALEATORIO DIRECTO");
        int posicion = pedirPosicion();
        if (posicion < 0) {
            return;
        }
        int nuevoStock = Consola.leerEntero("Nuevo stock: ");

        System.out.println("Antes:   " + AccesoDirectoProductos.leerProducto(posicion));
        if (AccesoDirectoProductos.actualizarStockDirecto(posicion, nuevoStock)) {
            System.out.println("Después: " + AccesoDirectoProductos.leerProducto(posicion));
        }
    }

    /**
     * Pide una posición del fichero de acceso directo (las opciones 7 y 8 la necesitan las dos).
     * Las posiciones empiezan en 0, igual que en un array.
     *
     * Si se escribe una posición fuera del fichero, se vuelve a preguntar.
     *
     * @return la posición introducida (de 0 a total-1), o -1 si el fichero está vacío
     */
    private static int pedirPosicion() {
        long total = AccesoDirectoProductos.numeroRegistros();
        if (total == 0) {
            System.out.println("El fichero de acceso directo está vacío. Usa antes la opción 6.");
            return -1;
        }
        while (true) {
            int posicion = Consola.leerEntero("Posición (0 a " + (total - 1) + "): ");
            if (posicion >= 0 && posicion < total) {
                return posicion;
            }
            System.err.println("La posición " + posicion + " está fuera de los límites del archivo.");
        }
    }

    /**
     * Opción 9: convierte el inventario binario (.dat) a texto (.csv).
     * Para verificar la conversión basta con abrir {@code data/backup/inventario.csv} y compararlo con la opción 4.
     */
    private static void exportarCSV() {
        titulo("[FASE 6] EXPORTACIÓN A CSV");
        List<Producto> inventario = ProductoDao.cargarInventario();
        if (inventario.isEmpty()) {
            System.out.println("No hay productos que exportar.");
            return;
        }
        ExportadorCSV.exportarInventario(inventario);
    }
}
