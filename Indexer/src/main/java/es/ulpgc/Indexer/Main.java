package es.ulpgc.Indexer;

import es.ulpgc.Cleaner.Book;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose an output type for indexing:");
        System.out.println("1: CSV");
        System.out.println("2: DataMart");
        System.out.print("Enter your choice (1 or 2): ");

        String choice = scanner.nextLine();
        String outputType;

        switch (choice) {
            case "1":
                outputType = "csv";
                break;
            case "2":
                outputType = "datamart";
                break;
            default:
                System.err.println("Invalid choice. Please restart the program and enter 1 or 2.");
                return;
        }

        try {
            es.ulpgc.Cleaner.Cleaner cleaner = new es.ulpgc.Cleaner.Cleaner();
            List<Book> books = cleaner.processAllBooks("datalake");
            Indexer indexer = new Indexer();
            indexer.indexBooks(books, outputType);
        } catch (IOException e) {
            System.err.println("Error processing books: " + e.getMessage());
        }
    }
}


