import dao.DirectoryManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        DirectoryManager g = new DirectoryManager();
        String ruta = "";
        g.iniciarEstructura();

        System.out.println("Selecciona la ruta a inspeccionar: ");
        System.out.println("1. Logs");
        System.out.println("2. Backup");
        System.out.println("3. Productos");

        int eleccion = sc.nextInt();
        switch (eleccion) {
            case 1:
                ruta = "data/logs";
            case 2:
                ruta = "data/backup";
            case 3:
                ruta = "data/productos";
        }
        g.inspeccionarDirectorio(ruta);
    }
}
