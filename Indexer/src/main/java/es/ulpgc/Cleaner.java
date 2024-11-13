package es.ulpgc;

<<<<<<< Updated upstream

import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
import java.io.*;
=======
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
>>>>>>> Stashed changes
import java.util.regex.Pattern;

public class Cleaner {
    private static LanguageDetectorME languageDetector;

<<<<<<< Updated upstream
    // Load the language detection model once
    static {
        try (InputStream modelIn = new FileInputStream("src/main/java/es/npl/langdetect-183.bin")) {
            LanguageDetectorModel model = new LanguageDetectorModel(modelIn);
            languageDetector = new LanguageDetectorME(model);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Detects language and applies language-specific cleaning
    public static Book extractMetadataAndContent(String rawContent) {
        String title = "Unknown Title";
        String author = "Unknown Author";
        String releaseDate = "Unknown Date";
        String language = "Unknown Language";
        StringBuilder content = new StringBuilder();
=======
    private static final String STOPWORDS_FILE_PATH = "stopwords_en.txt"; // Path to your stopwords file
    private static final Set<String> STOPWORDS = loadStopwords(STOPWORDS_FILE_PATH);

    private static Set<String> loadStopwords(String filePath) {
        Set<String> stopwords = new HashSet<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                stopwords.add(line.trim().toLowerCase()); // Normalize to lowercase
            }
        } catch (IOException e) {
            System.err.println("Error loading stopwords from file: " + filePath);
            e.printStackTrace();
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
>>>>>>> Stashed changes

        boolean contentStarted = false;

        try (BufferedReader reader = new BufferedReader(new StringReader(rawContent))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Check for metadata fields
                if (!contentStarted) {
                    if (line.startsWith("Title:")) {
                        title = line.substring(6).trim();
                    } else if (line.startsWith("Author:")) {
                        author = line.substring(7).trim();
                    } else if (line.startsWith("Release Date:")) {
                        releaseDate = line.substring(13).trim();
                    } else if (line.startsWith("Language:")) {
                        language = line.substring(9).trim();
                    }
                }

                // Check for start of actual content
                if (line.contains("*** START OF THE PROJECT GUTENBERG EBOOK")) {
                    contentStarted = true;
                    continue; // Skip the marker line
                }

                // Check for end of actual content
                if (line.contains("*** END OF THE PROJECT GUTENBERG EBOOK")) {
                    break; // Stop reading if we reached the end marker
                }

<<<<<<< Updated upstream
                // Append lines only if within the main content
                if (contentStarted) {
                    content.append(line).append(" ");
=======
        Map<String, String> metadata = extractMetadata(content);

        int startIdx = content.indexOf("*** START OF THIS PROJECT GUTENBERG EBOOK");
        if (startIdx != -1) {
            content = content.substring(startIdx);  // Remove metadata part
        }

        String fullContent = content;
        List<String> words = cleanText(content);

        return new Book(
                metadata.get("title"),
                metadata.get("author"),
                metadata.get("date"),
                metadata.get("language"),
                metadata.get("credits"),
                metadata.get("ebook_number"),
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
>>>>>>> Stashed changes
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

<<<<<<< Updated upstream
        // Detect language
        String detectedLanguage = detectLanguage(content.toString());
        if (!detectedLanguage.equals("Unknown Language")) {
            language = detectedLanguage;
        }

        // Clean the content based on detected language
        String cleanedContent = cleanBasedOnLanguage(content.toString(), detectedLanguage);

        // Return a new Book object with metadata and cleaned content
        return new Book(title, author, releaseDate, language, cleanedContent);
=======
        return books;
>>>>>>> Stashed changes
    }

    private static String detectLanguage(String content) {
        Language bestLanguage = languageDetector.predictLanguage(content);
        return bestLanguage.getLang().equals("und") ? "Unknown Language" : bestLanguage.getLang();
    }

    private static String cleanBasedOnLanguage(String content, String language) {
        // Basic cleanup: lowercase and remove non-alphabetic characters
        content = content.toLowerCase().replaceAll("[^a-zA-Z\\s]", "");

        // Apply language-specific cleaning rules if needed
        if (language.equals("en")) {
            // Additional cleaning for English, e.g., stopword removal (not shown here for brevity)
            content = removeEnglishStopWords(content);
        } else if (language.equals("es")) {
            // Additional cleaning for Spanish, e.g., stopword removal (not shown here for brevity)
            content = removeSpanishStopWords(content);
        }
        // Add more language conditions if needed

        return content;
    }

    // Example method for removing English stopwords (implement your own stopword lists)
    private static String removeEnglishStopWords(String content) {
        String[] stopwords = {"the", "and", "in", "of", "a", "to"}; // Sample stop words
        for (String stopword : stopwords) {
            content = content.replaceAll("\\b" + Pattern.quote(stopword) + "\\b", "");
        }
        return content.replaceAll("\\s+", " ").trim(); // Clean up extra spaces
    }

    // Example method for removing Spanish stopwords (implement your own stopword lists)
    private static String removeSpanishStopWords(String content) {
        String[] stopwords = {"el", "y", "en", "de", "la", "a"}; // Sample stop words for Spanish
        for (String stopword : stopwords) {
            content = content.replaceAll("\\b" + Pattern.quote(stopword) + "\\b", "");
        }
        return content.replaceAll("\\s+", " ").trim(); // Clean up extra spaces
    }
}
