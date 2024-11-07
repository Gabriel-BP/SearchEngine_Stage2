package es.ulpgc;

public class Book {
    private String title;
    private String author;
    private String publicationDate;
    private String language;
    private String content;

    public Book(String title, String author, String publicationDate, String language, String content) {
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.language = language;
        this.content = content;
    }

    // Getters for all attributes
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublicationDate() { return publicationDate; }
    public String getLanguage() { return language; }
    public String getContent() { return content; }
}
