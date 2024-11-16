package es.ulpgc.Indexer;

import es.ulpgc.Cleaner.Book;
import es.ulpgc.Cleaner.Cleaner;
import es.ulpgc.FileManager;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            es.ulpgc.Cleaner.Cleaner cleaner = new es.ulpgc.Cleaner.Cleaner();
            List<Book> books = cleaner.processAllBooks("datalake/" + FileManager.getDate());
            Indexer indexer = new Indexer();
            indexer.indexBooks(books);
        } catch (IOException e) {
            System.err.println("Error processing books: " + e.getMessage());
        }
    }
}
