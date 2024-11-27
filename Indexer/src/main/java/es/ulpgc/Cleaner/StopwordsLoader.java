package es.ulpgc.Cleaner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StopwordsLoader {
    private static final String STOPWORDS_FILE_PATH = "/app/shared/stopwords-en.txt"; // Ruta al archivo de stopwords

    public static Set<String> loadStopwords() {
        Set<String> stopwords = new HashSet<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(STOPWORDS_FILE_PATH));
            for (String line : lines) {
                stopwords.add(line.trim().toLowerCase()); // Normalizamos a minúsculas
            }
        } catch (IOException e) {
            System.err.println("Error cargando las stopwords desde el archivo: " + STOPWORDS_FILE_PATH);
        }
        return stopwords;
    }
}
