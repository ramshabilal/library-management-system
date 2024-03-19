package cs.nyuad.csuh3260.library;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

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
        // Create a book
        Book book = new Book("The Catcher in the Rye", "J.D. Salinger");

        // Test getters
        assertEquals("The Catcher in the Rye", book.getTitle());
        assertEquals("J.D. Salinger", book.getAuthor());
    }

    @Test
    public void testSetTitle() {
        // Create a book
        Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald");

        // Set a new title
        book.setTitle("To Kill a Mockingbird");

        // Check if the title has been updated
        assertEquals("To Kill a Mockingbird", book.getTitle());
    }

    @Test
    public void testSetAuthor() {
        // Create a book
        Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald");

        // Set a new author
        book.setAuthor("Harper Lee");

        // Check if the author has been updated
        assertEquals("Harper Lee", book.getAuthor());
    }

}