package util;

import model.Producto;

import java.util.ArrayList;
import java.util.List;

/**
 * Productos de ejemplo para probar la aplicación sin tener que meterlos a mano.
 * <p>
 * Los usan tanto {@code Main} (la prueba automática) como {@code MainApp} (opción 5 del menú),
 * así si queremos cambiar los datos de prueba solo hay que tocarlos aquí.
 */
public class DatosPrueba {

    /**
     * Devuelve una lista nueva con los productos de prueba.
     * <p>
     * creamos la lista cada vez que se llama (en vez de guardarla en una constante) para que si alguien
     * la modifica (añade, borra, cambia stock...) no afecte a la siguiente vez que se pidan los datos.
     *
     * @return lista de productos de ejemplo
     */
    public static List<Producto> productos() {
        List<Producto> prueba = new ArrayList<>();
        prueba.add(new Producto(1, "Teclado mecánico", 59.99, 20, "Periféricos"));
        prueba.add(new Producto(2, "Monitor 27\"", 219.50, 8, "Pantallas"));
        prueba.add(new Producto(3, "Ratón inalámbrico", 24.90, 35, "Periféricos"));
        prueba.add(new Producto(4, "Servidor Rack 1U", 1250.50, 3, "Servidores"));
        prueba.add(new Producto(5, "Switch gestionable 24p", 340.00, 15, "Redes"));
        return prueba;
    }
}
