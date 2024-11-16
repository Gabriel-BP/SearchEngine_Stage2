package es.ulpgc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InvertedIndex {
    private Map<String, Set<String>> index;

    public InvertedIndex() {
        this.index = new HashMap<>();
    }

    public void loadIndex(String contentFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(contentFilePath))) {
            String line;
            // Leer la primera línea (cabecera) y descartarla
            br.readLine();

            // Leer cada línea del archivo CSV
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String word = parts[0];
                Set<String> ebookNumbers = new HashSet<>();

                // Agregar todos los ebookNumbers al conjunto
                for (int i = 1; i < parts.length; i++) {
                    ebookNumbers.add(parts[i]);
                }

                // Añadir la palabra y sus ebookNumbers al índice
                index.put(word, ebookNumbers);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    public Set<String> search(String term) {
        return index.getOrDefault(term, new HashSet<>());
    }



}
