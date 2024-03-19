package cs.nyuad.csuh3260.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    private Book book;

    @BeforeEach
    public void setUp() {
        // Create a book
        book = new Book("The Great Gatsby", "F. Scott Fitzgerald");
    }

    @Test
    public void testBookCreation() {
        // Create some books
        Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald");
        Book book2 = new Book("To Kill a Mockingbird", "Harper Lee");
        Book book3 = new Book("1984", "George Orwell");

        // Test that IDs are not null and unique
        assertNotNull(book1.getID());
        assertNotNull(book2.getID());
        assertNotNull(book3.getID());
        assertNotEquals(book1.getID(), book2.getID());
        assertNotEquals(book1.getID(), book3.getID());
        assertNotEquals(book2.getID(), book3.getID());
    }

    @Test
    public void testGetters() {
        // Test getters
        assertEquals("The Great Gatsby", book.getTitle());
        assertEquals("F. Scott Fitzgerald", book.getAuthor());
    }

    @Test
    public void testSetTitle() {
        // Set a new title
        book.setTitle("To Kill a Mockingbird");

        // Check if the title has been updated
        assertEquals("To Kill a Mockingbird", book.getTitle());
    }

    @Test
    public void testSetAuthor() {
        // Set a new author
        book.setAuthor("Harper Lee");

        // Check if the author has been updated
        assertEquals("Harper Lee", book.getAuthor());
    }

}
