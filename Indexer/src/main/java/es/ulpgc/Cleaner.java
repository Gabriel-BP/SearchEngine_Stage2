package es.ulpgc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cleaner {

    private static final String STOPWORDS_FILE_PATH = "stopwords-en.txt"; // Path to your stopwords file
    private static final Set<String> STOPWORDS = loadStopwords();

    private static Set<String> loadStopwords() {
        Set<String> stopwords = new HashSet<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(Cleaner.STOPWORDS_FILE_PATH));
            for (String line : lines) {
                stopwords.add(line.trim().toLowerCase()); // Normalize to lowercase
            }
        } catch (IOException e) {
            System.err.println("Error loading stopwords from file: " + Cleaner.STOPWORDS_FILE_PATH);
        }
        return stopwords;
    }

    public static class Book {
        String title;
        String author;
        String date;
        String language;
        String credits;
        String ebookNumber;
        List<String> words;
        String fullContent;

        public Book(String title, String author, String date, String language, String credits, String ebookNumber, List<String> words, String fullContent) {
            this.title = title;
            this.author = author;
            this.date = date;
            this.language = language;
            this.credits = credits;
            this.ebookNumber = ebookNumber;
            this.words = words;
            this.fullContent = fullContent;
        }
    }

    public static List<String> cleanText(String text) {
        List<String> meaningfulWords = new ArrayList<>();
        String[] words = text.toLowerCase().split("\\W+");

        for (String word : words) {
            word = word.replace("_", "").trim();
            if (!STOPWORDS.contains(word) && word.length() > 2 && !word.matches("\\d+")) {
                meaningfulWords.add(word);
            }
        }
        return meaningfulWords;
    }

    private static Map<String, String> extractMetadata(String content) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("title", extractField("Title:\\s*(.+)", content, "Unknown Title"));
        metadata.put("author", extractField("Author:\\s*(.+)", content, "Unknown Author"));
        metadata.put("date", extractField("Release date:\\s*(\\d{4})", content, "Unknown Date"));
        metadata.put("language", extractField("Language:\\s*(.+)", content, "Unknown Language"));
        metadata.put("credits", extractField("Credits:\\s*(.+)", content, "Unknown Credits"));
        metadata.put("ebook_number", extractField("eBook (#\\d+)", content, "Unknown eBook Number"));
        return metadata;
    }

    private static String extractField(String regex, String content, String defaultValue) {
        Matcher matcher = Pattern.compile(regex, Pattern.MULTILINE).matcher(content);
        return matcher.find() ? matcher.group(1).trim() : defaultValue;
    }

    private static Book processBook(File file) throws IOException {
        String content = new String(Files.readAllBytes(file.toPath()));

        Map<String, String> metadata = extractMetadata(content);

        int startIdx = content.indexOf("*** START OF THIS PROJECT GUTENBERG EBOOK");
        if (startIdx != -1) {
            content = content.substring(startIdx);  // Remove metadata part
        }

        String fullContent = content;
        List<String> words = cleanText(content);
        String ebookNumber = file.getName().replaceFirst("[.][^.]+$", "");  // Remove extension

        return new Book(
                metadata.get("title"),
                metadata.get("author"),
                metadata.get("date"),
                metadata.get("language"),
                metadata.get("credits"),
                ebookNumber,
                words,
                fullContent
        );
    }

    public static List<Book> processAllBooks(String folderPath) throws IOException {
        File folder = new File(folderPath);
        List<Book> books = new ArrayList<>();

        if (folder.exists() && folder.isDirectory()) {
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if (file.isFile() && (file.getName().endsWith(".txt") || file.getName().endsWith(".html"))) {
                    System.out.println("Processing " + file.getName());
                    books.add(processBook(file));
                }
            }
        }

        return books;
    }

    public static void main(String[] args) throws IOException {
        // Example: Process all books from a folder and print out clean text of each book
        List<Book> books = processAllBooks("datalake/" + FileManager.getDate());

    }
}