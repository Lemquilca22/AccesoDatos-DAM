package dao;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionDB {

    private static final Path RUTA = Path.of("db.properties");
    private static String url, usuario, password;

    static {
        Properties prop = new Properties();
        try (Reader r = Files.newBufferedReader(RUTA)) {
            prop.load(r);
        } catch (IOException e) {
            throw new RuntimeException("No se ha podido leer el archivo db.properties" + e.getMessage());
        }
        url = prop.getProperty("url");
        usuario = prop.getProperty("usuario");
        password = prop.getProperty("password");
    }

    public static Connection getConnection() throws SQLException {
        return  DriverManager.getConnection(url, usuario, password);
    }
}
