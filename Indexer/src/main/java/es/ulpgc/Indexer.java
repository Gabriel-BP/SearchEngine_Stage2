package es.ulpgc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Indexer {

    private static final String INDEX_METADATA_FILE = "index_metadata.csv";
    private static final String INDEX_CONTENT_FILE = "index_content.csv";

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
                    return Collections.emptySet();
                }
            }
            return node.ebookNumbers;
        }
    }

    private final Map<String, Set<String>> hashMapIndex = new HashMap<>();
    private final Trie trieIndex = new Trie();

    public void addWordToHashMapIndex(String word, String ebookNumber) {
        hashMapIndex.computeIfAbsent(word, k -> new HashSet<>()).add(ebookNumber);
    }

    public void addWordToTrieIndex(String word, String ebookNumber) {
        trieIndex.insert(word, ebookNumber);
    }

    public void saveMetadataToCSV(List<Cleaner.Book> books) {
        try (FileWriter metadataWriter = new FileWriter(INDEX_METADATA_FILE)) {
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
            System.out.println("Metadata saved to " + INDEX_METADATA_FILE);
        } catch (IOException e) {
            System.err.println("Error writing metadata to CSV: " + e.getMessage());
        }
    }

    public void saveContentToCSV(List<Cleaner.Book> books) {
        try (FileWriter contentWriter = new FileWriter(INDEX_CONTENT_FILE)) {
            contentWriter.append("Word,EbookNumbers\n");

            // Use a Map to store each word with a set of eBook numbers where it appears
            Map<String, Set<String>> wordToEbookNumbers = new HashMap<>();

            // Iterate through each book and its words
            for (Cleaner.Book book : books) {
                for (String word : book.words) {
                    wordToEbookNumbers
                            .computeIfAbsent(word, k -> new HashSet<>())
                            .add(book.ebookNumber);  // Add the eBook number where the word appears
                }
            }

            // Now write the words and their associated eBook numbers
            for (Map.Entry<String, Set<String>> entry : wordToEbookNumbers.entrySet()) {
                String word = entry.getKey();
                String ebookNumbers = String.join(",", entry.getValue());  // Join eBook numbers with commas
                contentWriter.append(word)
                        .append(",").append(ebookNumbers)
                        .append("\n");
            }

            contentWriter.flush();
            System.out.println("Content index saved to " + INDEX_CONTENT_FILE);
        } catch (IOException e) {
            System.err.println("Error writing content index to CSV: " + e.getMessage());
        }
    }


    public void buildIndexes(List<Cleaner.Book> books) {
        for (Cleaner.Book book : books) {
            for (String word : book.words) {
                addWordToHashMapIndex(word, book.ebookNumber);
                addWordToTrieIndex(word, book.ebookNumber);
            }
        }
    }

    public void indexBooks(List<Cleaner.Book> books) {
        try {
            buildIndexes(books);  // Build both Trie and HashMap indexes
            saveMetadataToCSV(books);
            saveContentToCSV(books);
            System.out.println("Indexing completed and saved to CSV files.");
        } catch (Exception e) {
            System.err.println("Error during indexing: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            List<Cleaner.Book> books = Cleaner.processAllBooks("datalake/" + FileManager.getDate());
            Indexer indexer = new Indexer();
            indexer.indexBooks(books);
        } catch (IOException e) {
            System.err.println("Error processing books: " + e.getMessage());
        }
    }
}
