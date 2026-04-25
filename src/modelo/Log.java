package modelo;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Log {
    private String nombreArchivo;
    private List<String> turnos; // Almacenamos las líneas ya formateadas

    public Log() {
        this.turnos = new ArrayList<>();
        // Formato para el nombre del archivo: YYYYMMDDHHmmss
        String fechaFile = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss'_LOG'"));
        
        // Crear carpeta si no existe
        File folder = new File("logs");
        if (!folder.exists()) folder.mkdir();
        
        this.nombreArchivo = "logs/" + fechaFile + ".log";
    }

    public void registrarTurno(String evento, String infoPk1, String infoPk2, int numTurno) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        
        // Formato: 2023-02-22 21:17:58.672 INFO evento pokemon = {info}, pokemonRival={info}, turno=X
        String linea = String.format("%s INFO %s pokemon = {%s}, pokemonRival={%s}, turno=%d",
                                    timestamp, evento, infoPk1, infoPk2, numTurno);
        
        turnos.add(linea);
        escribirLinea(linea);
    }

    private void escribirLinea(String linea) {
        try (FileWriter fw = new FileWriter(nombreArchivo, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(linea);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}