package cs.nyuad.csuh3260.library;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    @Test
    public void testBookCreation() {
        // Create some books
        Book book1 = new Book();
        Book book2 = new Book();
        Book book3 = new Book();

        // Test that IDs are not null and unique
        assertNotNull(book1.getID());
        assertNotNull(book2.getID());
        assertNotNull(book3.getID());
        assertNotEquals(book1.getID(), book2.getID());
        assertNotEquals(book1.getID(), book3.getID());
        assertNotEquals(book2.getID(), book3.getID());
    }

}