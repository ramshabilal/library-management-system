package cs.nyuad.csuh3260.library;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    private Book book;

    public void setUp() {
        // Create a book
        book = new Book(UUID.randomUUID().toString(), "The Great Gatsby", "F. Scott Fitzgerald", 1);
    }

    @Test
    public void testBookCreation() {
        // Create some books
        setUp();
        Book book2 = new Book(UUID.randomUUID().toString(), "To Kill a Mockingbird", "Harper Lee", 1);
        Book book3 = new Book(UUID.randomUUID().toString(), "1984", "George Orwell", 1);

        // Test that IDs are not null and unique
        assertNotNull(book.getID());
        assertNotNull(book2.getID());
        assertNotNull(book3.getID());
        assertNotEquals(book.getID(), book2.getID());
        assertNotEquals(book.getID(), book3.getID());
        assertNotEquals(book2.getID(), book3.getID());
    }

    @Test
    public void testGetters() {
        setUp();
        // Test getters
        assertEquals("The Great Gatsby", book.getTitle());
        assertEquals("F. Scott Fitzgerald", book.getAuthor());
    }

    @Test
    public void testSetTitle() {
        setUp();
        // Set a new title
        book.setTitle("To Kill a Mockingbird");

        // Check if the title has been updated
        assertEquals("To Kill a Mockingbird", book.getTitle());
    }

    @Test
    public void testSetAuthor() {
        setUp();
        // Set a new author
        book.setAuthor("Harper Lee");

        // Check if the author has been updated
        assertEquals("Harper Lee", book.getAuthor());
    }

    @Test
    public void testSetCount() {
        setUp();
        // Set a new author
        book.setCount(10);

        // Check if the author has been updated
        assertEquals(10, book.getCount());
    }

}
