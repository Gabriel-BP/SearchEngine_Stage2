package es.ulpgc;


import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
import java.io.*;
import java.util.regex.Pattern;

public class Cleaner {
    private static LanguageDetectorME languageDetector;

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

                // Append lines only if within the main content
                if (contentStarted) {
                    content.append(line).append(" ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Detect language
        String detectedLanguage = detectLanguage(content.toString());
        if (!detectedLanguage.equals("Unknown Language")) {
            language = detectedLanguage;
        }

        // Clean the content based on detected language
        String cleanedContent = cleanBasedOnLanguage(content.toString(), detectedLanguage);

        // Return a new Book object with metadata and cleaned content
        return new Book(title, author, releaseDate, language, cleanedContent);
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
