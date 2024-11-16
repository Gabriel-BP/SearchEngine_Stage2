package es.ulpgc;

import java.util.List;

public class Main {
    // Create a new instance of the GutenbergCrawler class
    public static void main(String[] args) {
        GutenbergCrawler crawler = new GutenbergCrawler();
        // print all folders in the path
        List<String> folders = GutenbergCrawler.getFoldersInPath();
        folders.forEach(System.out::println);
        // print the earliest non-empty folder
        String earliestFolder = GutenbergCrawler.getLatestNonEmptyFolder();
        System.out.println("Earliest non-empty folder: " + earliestFolder);
        // print the file with the largest book ID in the earliest non-empty folder
        String largestFile = GutenbergCrawler.getFileWithLargestBookID(earliestFolder);
        System.out.println("File with largest book ID: " + largestFile);
        // download a number of books
        crawler.crawlBooks(100);
    }
}
