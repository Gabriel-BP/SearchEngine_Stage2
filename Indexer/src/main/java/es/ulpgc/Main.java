package es.ulpgc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String folderPath = "gutenberg_books"; // Folder containing the book text files

        // Read and process each file in the folder
        try (Stream<Path> filePathStream = Files.walk(Paths.get(folderPath))) {
            filePathStream
                    .filter(Files::isRegularFile) // Only process files, not directories
                    .forEach(filePath -> {
                        try {
                            // Read the raw content of the file
                            String rawContent = new String(Files.readAllBytes(filePath));

                            // Use Cleaner to extract metadata and cleaned content
                            Book book = Cleaner.extractMetadataAndContent(rawContent);

                            // Print out the extracted metadata and cleaned content for verification
                            System.out.println("Title: " + book.getTitle());
                            System.out.println("Author: " + book.getAuthor());
                            System.out.println("Release Date: " + book.getPublicationDate());
                            System.out.println("Language: " + book.getLanguage());
                            System.out.println("Cleaned Content Preview: " +
                                    book.getContent().substring(0, Math.min(200, book.getContent().length())) + "...");
                            System.out.println("------------------------------------------------------");

                        } catch (IOException e) {
                            System.err.println("Failed to read file: " + filePath);
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            System.err.println("Failed to access folder: " + folderPath);
            e.printStackTrace();
        }
    }
}
