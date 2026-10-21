package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogger {
    private static final Path RUTA_LOG = Paths.get("data/logs/audit.log");

    public static void registrarLog(String nivelEvento, String descripcion) {
        try {
            Files.createDirectories(RUTA_LOG.getParent());

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = LocalDateTime.now().format(dtf);

            String lineaLog = "[" + timestamp + "] [" + nivelEvento + "] " + descripcion;

            //Notas para entender el codigo:
            //BufferedWriter sirve para que en lugar de pasar letra por letra al disco duro lo que hace es juntar todos los datos
            //en la RAM y escribirlos de una.
            try (BufferedWriter writer = Files.newBufferedWriter(RUTA_LOG, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write(lineaLog);
                writer.newLine();
            }
            System.out.println("Log registrado: " + lineaLog);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public static void buscarPalabraClave(String palabraClave) {
        try (BufferedReader reader = Files.newBufferedReader(RUTA_LOG, StandardCharsets.UTF_8)) {
            String linea;
            int contador=0;
            while ((linea = reader.readLine()) != null) {
                if (linea.contains(palabraClave)) {
                    System.out.println("  -> Coincidencia: " + linea);
                    contador++;
                }
            }
            System.out.println("Búsqueda finalizada. Total de coincidencias: " + contador);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
