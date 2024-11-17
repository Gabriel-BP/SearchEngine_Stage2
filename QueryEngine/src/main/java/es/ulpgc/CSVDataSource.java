package es.ulpgc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CSVDataSource implements DataSource {
    private final String contentFilePath;

    public CSVDataSource(String contentFilePath) {
        this.contentFilePath = contentFilePath;
    }

    @Override
    public Map<String, Set<String>> loadIndex() {
        Map<String, Set<String>> index = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(contentFilePath))) {
            String line;
            br.readLine(); // Saltar la cabecera
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String word = parts[0];
                Set<String> ebookNumbers = new HashSet<>();
                for (int i = 1; i < parts.length; i++) {
                    ebookNumbers.add(parts[i]);
                }
                index.put(word, ebookNumbers);
            }
        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
        return index;
    }


}
