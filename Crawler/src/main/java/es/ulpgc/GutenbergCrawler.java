package es.ulpgc;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GutenbergCrawler {

    private static final String BASE_URL = "https://www.gutenberg.org/";
    private static final String DOWNLOAD_FOLDER = "datalake";
    private static final String date = getDate();
    private static final String webID = "01";



    public static String getDate() {
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyy");
        return hoy.format(format);
    }
    // Create download folder if it doesn't exist
    static {
        try {
            Files.createDirectories(Paths.get(DOWNLOAD_FOLDER+"/"+date));
        } catch (IOException e) {
            System.err.println("Could not create Datalake folder: " + e.getMessage());
        }
    }

    // Method to download a book from Project Gutenberg
    public void downloadBook(String bookId) {
        String[] formats = {"txt", "html", "epub", "mobi"};
        for (String format : formats) {
            String url = BASE_URL + "cache/epub/" + bookId + "/pg" + bookId + "." + format;
            String newID = webID + bookId;
            String filename = DOWNLOAD_FOLDER + "/" + date + "/" + newID + "." + format;
            try {
                InputStream in = Jsoup.connect(url).ignoreContentType(true).execute().bodyStream();
                Files.copy(in, Paths.get(filename), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Downloaded " + filename);
                return;
            } catch (IOException e) {
                System.err.println("Could not download " + url + ": " + e.getMessage());
                e.printStackTrace();
            }

        }
        System.out.println("Book " + bookId + " is not available in any of the supported formats.");
    }

    // Method to get the list of folders in the download path
    public static List<String> getFoldersInPath() {
        List<String> folders = new ArrayList<>();
        File folder = new File(DOWNLOAD_FOLDER);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        folders.add(file.getName());
                    }
                }
            }
        }

        return folders;
    }

    // Method to get the latest non-empty folder in the download path
    public static String getLatestNonEmptyFolder() {
        List<String> folders = getFoldersInPath();
        String latestFolder = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");

        for (String folderName : folders) {
            try {
                LocalDate folderDate = LocalDate.parse(folderName, formatter);

                // Check if the folder is non-empty
                File folder = new File(DOWNLOAD_FOLDER, folderName);
                if (folder.listFiles() != null && folder.listFiles().length > 0) {
                    // Update latestFolder if it's the first valid folder or a later date
                    if (latestFolder == null || folderDate.isAfter(LocalDate.parse(latestFolder, formatter))) {
                        latestFolder = folderName;
                    }
                }
            } catch (Exception e) {
                System.out.println("Skipping invalid folder format: " + folderName);
            }
        }

        if (latestFolder == null) {
            System.out.println("No non-empty folders found.");
        }

        return latestFolder;
    }


    public static String getFileWithLargestBookID(String latestFolder) {
        if (latestFolder == null) {
            System.err.println("No non-empty folder available.");
            return "010.txt";
        }

        File folder = new File(DOWNLOAD_FOLDER, latestFolder);
        File[] files = folder.listFiles();
        String largestFile = null;
        int largestBookID = -1;

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String filename = file.getName();
                    if (filename.length() > 2) {
                        try {
                            // Extract the book ID
                            String bookIDStr = filename.substring(2, filename.lastIndexOf('.'));
                            int bookID = Integer.parseInt(bookIDStr);

                            // Find the file with the largest book ID
                            if (bookID > largestBookID) {
                                largestBookID = bookID;
                                largestFile = filename;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Skipping invalid file format: " + filename);
                        }
                    }
                }
            }
        }

        return largestFile;
    }


    // Method to crawl Project Gutenberg and download a number of books
    public void crawlBooks(int numBooks) {
        String latestID = getFileWithLargestBookID(getLatestNonEmptyFolder());
        int ID = Integer.parseInt(latestID.substring(2, latestID.lastIndexOf('.')));
        int startIndex = ID + 1;
        int endIndex = startIndex + numBooks;
        for (int i = startIndex; i < endIndex; i++) {
            downloadBook(String.valueOf(i));
        }
    }

}