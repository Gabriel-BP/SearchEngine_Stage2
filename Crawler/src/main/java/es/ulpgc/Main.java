package es.ulpgc;

import java.util.List;

public class Main {
    // Create a new instance of the GutenbergCrawler class
    public static void main(String[] args) {
        GutenbergCrawler crawler = new GutenbergCrawler();

        System.out.println("Starting crawling process...");
        crawler.crawlBooks(100);
        System.out.println("Crawling completed.");
    }
}
