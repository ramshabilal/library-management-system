package cs.nyuad.csuh3260.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class DatabaseManagerTest {

    private DatabaseManager databaseManager;

    @BeforeEach
    public void setUp() {
        databaseManager = new DatabaseManager();
    }

    @Test
    public void testGetBooks() {
        // When
        List<Book> books = databaseManager.getBooks();

        // Then
        assertNotNull(books);
        assertTrue(books.isEmpty());
    }
}
