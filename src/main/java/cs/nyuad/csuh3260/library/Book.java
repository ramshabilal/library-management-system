package cs.nyuad.csuh3260.library;

public class Book {

    private String id;
    private String title;
    private String author;
    private Integer count;

    public Book(String id, String title, String author, Integer count) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.count = count;
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

    public Integer getCount() {
        return count;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}