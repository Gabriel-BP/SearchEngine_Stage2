package es.ulpgc.Cleaner;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Cleaner cleaner = new Cleaner();

        // Specify the root path for the datalake
        String rootPath = "datalake";

        // Process all the books across all subfolders, starting from the last processed file
        List<Book> books = cleaner.processAllBooks(rootPath);

        // Optionally, print out the number of books processed
        System.out.println("Processed " + books.size() + " books.");
    }
}


