package es.ulpgc;

public class GutenbergCrawler {
    private static final String DOWNLOAD_FOLDER = "datalake";
    private static final String webID = "01";

    public void crawlBooks(int numBooks) {
        String date = FileManager.getDate();
        String folderPath = DOWNLOAD_FOLDER + "/" + date;
        FileManager.createFolder(folderPath);

        String latestFolder = FileManager.getLatestNonEmptyFolder();
        String latestFile = FileManager.getFileWithLargestBookID(latestFolder);
        int startIndex = latestFile != null ? Integer.parseInt(latestFile.substring(2, latestFile.lastIndexOf('.'))) + 1 : 1;

        for (int i = startIndex; i < startIndex + numBooks; i++) {
            boolean success = GutenbergDownloader.downloadBook(String.valueOf(i), folderPath, webID);
            if (!success) {
                System.out.println("Skipping book ID " + i);
            }
        }
    }
}
