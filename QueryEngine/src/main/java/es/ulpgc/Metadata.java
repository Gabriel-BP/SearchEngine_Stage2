package es.ulpgc;

public class Metadata {
    private final String title;
    private final String author;
    private final String date;
    private final String language;
    private final String credits;

    public Metadata(String title, String author, String date, String language, String credits) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.language = language;
        this.credits = credits;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getLanguage() {
        return language;
    }

    public String getCredits() {
        return credits;
    }

    @Override
    public String toString() {
        return String.format("Title: %s, Author: %s, Date: %s, Language: %s, Credits: %s", title, author, date, language, credits);
    }
}
