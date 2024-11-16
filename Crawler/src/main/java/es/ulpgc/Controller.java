package es.ulpgc;

public class Controller {
    public static void main(String[] args) {
        GutenbergCrawler crawler = new GutenbergCrawler();

        System.out.println("Starting crawling process...");
        crawler.crawlBooks(100);
        System.out.println("Crawling completed.");
    }
}
