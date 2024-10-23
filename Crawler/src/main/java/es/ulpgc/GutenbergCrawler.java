package es.ulpgc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GutenbergCrawler {

    private static final String BASE_URL = "https://www.gutenberg.org";
    private static final String BOOKS_URL = BASE_URL + "/browse/scores/top";
    private static final String DOWNLOAD_FOLDER = "gutenberg_books";

    // Create download folder if it doesn't exist
    static {
        try {
            Files.createDirectories(Paths.get(DOWNLOAD_FOLDER));
        } catch (IOException e) {
            System.err.println("Could not create download folder: " + e.getMessage());
        }
    }

    // Method to fetch book links
    public List<String> getBookLinks() {
        List<String> bookLinks = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(BOOKS_URL).header("Accept-Encoding", "identity").get();
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String href = link.attr("href");
                if (href.startsWith("/ebooks/")) {
                    bookLinks.add(BASE_URL + href);
                }
            }
        } catch (IOException e) {
            System.err.println("Error fetching book links: " + e.getMessage());
        }
        return bookLinks;
    }


    // Method to download a book by ID
    public void downloadBook(String bookId) {
        String[] formats = {"txt", "html", "epub", "mobi"};
        for (String format : formats) {
            String url = String.format("https://gutenberg.org/cache/epub/%s/pg%s.%s", bookId, bookId, format);
            Path filePath = Paths.get(DOWNLOAD_FOLDER, bookId + "." + format);

            // Check if the book is already downloaded
            if (Files.exists(filePath)) {
                System.out.println(bookId + "." + format + " already downloaded, skipping.");
                return;
            }

            System.out.println("Attempting to download book " + bookId + " from " + url);
            try (InputStream in = Jsoup.connect(url).ignoreContentType(true).execute().bodyStream()) {
                Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Downloaded book " + bookId + " in " + format + " format.");
                return;  // Exit once a format is downloaded
            } catch (IOException e) {
                System.err.println("Error downloading from " + url + ": " + e.getMessage());
            }
        }
        System.out.println("Book " + bookId + " is not available in any of the supported formats.");
    }

    // Method to crawl through and download a specified number of books
    public void crawlBooks(int numBooks) {
        List<String> bookLinks = getBookLinks();
        if (bookLinks.isEmpty()) {
            System.out.println("No book links found. Exiting crawler.");
            return;
        }

        List<String> bookIds = new ArrayList<>();
        for (String link : bookLinks) {
            bookIds.add(link.substring(link.lastIndexOf('/') + 1));
        }

        for (int i = 0; i < Math.min(numBooks, bookIds.size()); i++) {
            downloadBook(bookIds.get(i));
            try {
                TimeUnit.SECONDS.sleep(1);  // Pause between downloads
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Crawl interrupted: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        GutenbergCrawler crawler = new GutenbergCrawler();
        crawler.crawlBooks(10);  // Example: download 5 books
    }
}