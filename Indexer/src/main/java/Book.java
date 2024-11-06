public class Book {
    private String title;
    private String author;
    private String publicationDate;
    private String content;

    public Book(String title, String author, String publicationDate, String content) {
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.content = content;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublicationDate() { return publicationDate; }
    public String getContent() { return content; }
}