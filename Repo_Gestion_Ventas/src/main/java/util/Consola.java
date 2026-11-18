package util;

import dao.AuditLogger;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Lectura segura de datos por teclado.
 * <p>
 * La sacamos de {@code MainApp} para que el menú solo se encargue de mostrar opciones y llamar a los DAO.
 * Si mañana hacemos otro menú (por ejemplo, de usuarios o pedidos), reutilizamos esta clase tal cual.
 *
 * @see <a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Scanner.html">Scanner (Oracle Java 17)</a>
 */
public class Consola {

    /**
     * Único Scanner para toda la aplicación.
     * <p>
     * Notas para entender el codigo:
     * solo creamos uno porque si cerramos un Scanner de {@code System.in} se cierra también el teclado
     * para el resto del programa, y ya no se podría leer nada más.
     */
    private static final Scanner TECLADO = new Scanner(System.in);

    /**
     * Lee una línea de texto no vacía.
     * <p>
     * Si el usuario cierra la entrada (Ctrl+D / Ctrl+Z), {@code nextLine()} lanza
     * {@link NoSuchElementException}; en ese caso salimos del programa de forma ordenada.
     *
     * @param mensaje texto que se muestra antes de leer
     * @return lo que ha escrito el usuario, sin espacios al principio ni al final
     */
    public static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea;
            try {
                linea = TECLADO.nextLine().trim();
            } catch (NoSuchElementException e) {
                System.out.println("\nEntrada cerrada, se sale de la aplicación.");
                AuditLogger.registrarLog("WARNING", "Entrada estándar cerrada, la aplicación termina.");
                System.exit(0);
                return ""; //Nunca llega aquí, pero el compilador lo necesita
            }
            if (!linea.isEmpty()) {
                return linea;
            }
            System.out.println("No puede estar vacío.");
        }
    }

    /**
     * Lee un número entero, repitiendo la pregunta hasta que se escriba uno válido.
     * <p>
     * Notas para entender el codigo:
     * leemos la línea como texto y la convertimos con {@code Integer.parseInt()}. Si el usuario escribe
     * "hola", parseInt lanza NumberFormatException, la capturamos y volvemos a preguntar. Así el programa
     * no peta por un error al teclear. (Usar nextInt() da problemas porque deja el salto de línea en el buffer.)
     *
     * @param mensaje texto que se muestra antes de leer
     * @return el número introducido
     */
    public static int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.err.println("'" + texto + "' no es un número entero válido.");
            }
        }
    }
}
