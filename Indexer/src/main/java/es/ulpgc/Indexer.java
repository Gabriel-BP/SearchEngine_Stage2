package es.ulpgc;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Indexer {

    private static final String INDEX_METADATA_FILE = "index_metadata.csv";
    private static final String INDEX_CONTENT_FILE = "index_content.csv";

    // Estructura de TrieNode y Trie para el índice invertido basado en Trie
    public static class TrieNode {
        Map<Character, TrieNode> children;
        Set<String> ebookNumbers;

        public TrieNode() {
            this.children = new HashMap<>();
            this.ebookNumbers = new HashSet<>();
        }
    }

    public static class Trie {
        private final TrieNode root;

        public Trie() {
            this.root = new TrieNode();
        }

        public void insert(String word, String ebookNumber) {
            TrieNode node = root;
            for (char ch : word.toCharArray()) {
                node.children.putIfAbsent(ch, new TrieNode());
                node = node.children.get(ch);
            }
            node.ebookNumbers.add(ebookNumber);
        }

        public Set<String> search(String word) {
            TrieNode node = root;
            for (char ch : word.toCharArray()) {
                node = node.children.get(ch);
                if (node == null) {
                    return Collections.emptySet(); // palabra no encontrada
                }
            }
            return node.ebookNumbers;
        }
    }

    // Implementación del índice invertido usando HashMap
    private final Map<String, Set<String>> hashMapIndex = new HashMap<>();
    private final Trie trieIndex = new Trie();

    // Método para añadir palabras al índice HashMap
    public void addWordToHashMapIndex(String word, String ebookNumber) {
        hashMapIndex.computeIfAbsent(word, k -> new HashSet<>()).add(ebookNumber);
    }

    // Método para añadir palabras al índice Trie
    public void addWordToTrieIndex(String word, String ebookNumber) {
        trieIndex.insert(word, ebookNumber);
    }

    // Método para guardar metadatos de los libros en un archivo CSV
    public void saveMetadataToCSV(List<Cleaner.Book> books) throws IOException {
        FileWriter metadataWriter = new FileWriter(INDEX_METADATA_FILE);
        // Escribir el encabezado CSV
        metadataWriter.append("ebookNumber,Title,Author,Date,Language,Credits\n");

        for (Cleaner.Book book : books) {
            metadataWriter.append(book.ebookNumber)
                    .append(",").append(book.title)
                    .append(",").append(book.author)
                    .append(",").append(book.date)
                    .append(",").append(book.language)
                    .append(",").append(book.credits)
                    .append("\n");
        }

        metadataWriter.flush();
        metadataWriter.close();
    }

    // Método para guardar el índice de contenido (palabras y su ebookNumber) en un archivo CSV
    public void saveContentToCSV(List<Cleaner.Book> books) throws IOException {
        FileWriter contentWriter = new FileWriter(INDEX_CONTENT_FILE);
        // Escribir el encabezado CSV
        contentWriter.append("Word,EbookNumber\n");

        // Recorrer las palabras de los libros y guardarlas en el archivo CSV
        for (Cleaner.Book book : books) {
            for (String word : book.words) {
                contentWriter.append(word)
                        .append(",").append(book.ebookNumber)
                        .append("\n");
            }
        }

        contentWriter.flush();
        contentWriter.close();
    }

    // Método principal para indexar metadatos y contenido de libros y guardarlos en CSV
    public void indexBooks(List<Cleaner.Book> books) {
        try {
            saveMetadataToCSV(books);
            saveContentToCSV(books);
            System.out.println("Indexing completed and saved to CSV files.");
        } catch (IOException e) {
            System.err.println("Error while saving index to CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        // Procesamos todos los libros en una carpeta
        List<Cleaner.Book> books = Cleaner.processAllBooks("datalake/" + GutenbergCrawler.getDate());

        // Crear un objeto Indexer para indexar los libros
        Indexer indexer = new Indexer();
        // Indexar los libros y guardarlos en archivos CSV
        indexer.indexBooks(books);
    }
}
