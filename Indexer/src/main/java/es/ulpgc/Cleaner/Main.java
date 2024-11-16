package es.ulpgc.Cleaner;

import es.ulpgc.FileManager;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Cleaner cleaner = new Cleaner();
        List<Book> books = cleaner.processAllBooks("datalake/" + FileManager.getDate());
    }
}
