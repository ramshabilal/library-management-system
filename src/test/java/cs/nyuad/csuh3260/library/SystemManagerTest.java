package cs.nyuad.csuh3260.library;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SystemManagerTest {
    private SystemManager system;
    private DatabaseManager databaseManager;

    @BeforeEach
    void setUp() {
        databaseManager = mock(DatabaseManager.class);
        system = new SystemManager();
    }

    @Test
    void testSearch_ExistingKeywords_ReturnsMatchingBooks() {
        Book book1 = new Book("Book 1", "Author 1");
        Book book2 = new Book("Book 2", "Author 2");
        List<Book> allBooks = Arrays.asList(book1, book2);
        when(databaseManager.getBooks()).thenReturn(allBooks);

        List<Book> result = system.search(Arrays.asList("Book"));

        if (result == null)
        {
            assertEquals(2, 0);
        }
        assertEquals(2, result.size());
        assertTrue(result.contains(book1));
        assertTrue(result.contains(book2));
        verify(databaseManager, times(1)).getBooks();
    }

    
}