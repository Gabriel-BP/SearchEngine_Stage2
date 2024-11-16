package es.ulpgc.Indexer;

import es.ulpgc.Cleaner.Book;
import java.util.List;

public class Indexer {
    private final BookIndexer bookIndexer;
    private final CSVWriter csvWriter;

    public Indexer() {
        this.bookIndexer = new BookIndexer();
        this.csvWriter = new CSVWriter();
    }

    public void buildIndexes(List<Book> books) {
        for (Book book : books) {
            bookIndexer.indexBook(book);
        }
    }

    public void indexBooks(List<Book> books) {
        try {
            buildIndexes(books);  // Index both HashMap and Trie
            csvWriter.saveMetadataToCSV(books);
            csvWriter.saveContentToCSV(bookIndexer.getHashMapIndexer().getIndex());
            System.out.println("Indexing completed and saved to CSV files.");
        } catch (Exception e) {
            System.err.println("Error during indexing: " + e.getMessage());
        }
    }

}
