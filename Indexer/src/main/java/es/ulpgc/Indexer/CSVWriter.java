package es.ulpgc.Indexer;
import es.ulpgc.Cleaner.Book;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class CSVWriter {
    private static final String INDEX_METADATA_FILE = "index_metadata.csv";
    private static final String INDEX_CONTENT_FILE = "index_content.csv";

    public void saveMetadataToCSV(Iterable<Book> books) {
        try (FileWriter metadataWriter = new FileWriter(INDEX_METADATA_FILE)) {
            metadataWriter.append("ebookNumber,Title,Author,Date,Language,Credits\n");
            for (Book book : books) {
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
    public void saveContentToCSV(Map<String, Set<String>> wordToEbookNumbers) {
        try (FileWriter contentWriter = new FileWriter(INDEX_CONTENT_FILE)) {
            contentWriter.append("Word,EbookNumbers\n");
            for (Map.Entry<String, Set<String>> entry : wordToEbookNumbers.entrySet()) {
                String word = entry.getKey();
                String ebookNumbers = String.join(",", entry.getValue());
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

}
