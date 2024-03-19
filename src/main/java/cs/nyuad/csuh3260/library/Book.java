package cs.nyuad.csuh3260.library;

import java.util.UUID;

public class Book {

    private String id;
    private String title;
    private String author;

    public Book(String title, String author) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
    }

    // Getters
    public String getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    // Setters
    public void setTitle(String title) {
    }

    public void setAuthor(String author) {
    }

}