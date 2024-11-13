package es.ulpgc;

import java.io.FileWriter;
import java.io.IOException;
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

    public void saveMetadataToCSV(List<Cleaner.Book> books) throws IOException {
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
        }
    }

    public void saveContentToCSV(List<Cleaner.Book> books) throws IOException {
        try (FileWriter contentWriter = new FileWriter(INDEX_CONTENT_FILE)) {
            contentWriter.append("Word,EbookNumber\n");

            for (Cleaner.Book book : books) {
                for (String word : book.words) {
                    contentWriter.append(word)
                            .append(",").append(book.ebookNumber)
                            .append("\n");
                }
            }
        }
    }

    public void indexBooks(List<Cleaner.Book> books) {
        for (Cleaner.Book book : books) {
            for (String word : book.words) {
                addWordToHashMapIndex(word, book.ebookNumber);
                addWordToTrieIndex(word, book.ebookNumber);
            }
        }
        try {
            saveMetadataToCSV(books);
            saveContentToCSV(books);
            System.out.println("Indexing completed and saved to CSV files.");
        } catch (IOException e) {
            System.err.println("Error while saving index to CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        // Process all books from the data lake
        String directoryPath = "datalake/" + GutenbergCrawler.getDate();
        List<Cleaner.Book> books = Cleaner.processAllBooks(directoryPath);

        // Create an instance of Indexer and index the books
        Indexer indexer = new Indexer();
        indexer.indexBooks(books);
    }
}
