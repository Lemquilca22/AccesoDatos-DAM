package dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Stream;

public class EjecucionScripts {

    public static void ejecutarScript (Path ruta) throws IOException, SQLException {
        String sql = Files.readString(Path.of(ruta.toString()));

        try (Connection conDb = ConexionDB.getConnection();
             Statement st = conDb.createStatement()) {;
            st.execute(sql);
            System.out.println("Procedure ejecutado correctamente");
        }
    }

    public static void ejecutarCarpeta (Path carpeta) throws IOException, SQLException {
        try (Stream<Path> archivos = Files.list(carpeta)) {
            for (Path p : archivos.filter(f -> f.toString().endsWith(".sql")).sorted().toList()) {
                ejecutarScript (p);
            }
        }
    }
}
