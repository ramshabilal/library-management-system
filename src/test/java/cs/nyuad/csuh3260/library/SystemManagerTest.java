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

    @Test
    void testReserve_ValidUserAndBook_ReservesBook() {
        when(databaseManager.getBooks()).thenReturn(Collections.singletonList(new Book("Book 1", "Author 1")));
        when(databaseManager.getUsers(anyList())).thenReturn(Collections.singletonList("1"));
    
        boolean result = system.reserve("1", "1");

        assertTrue(result);
        assertEquals(1, system.getBookings().get("1").size());
        assertEquals("1", system.getBookings().get("1").get(0));
        verify(databaseManager, times(1)).getBooks();
        verify(databaseManager, times(1)).getUsers(anyList());
    }

    @Test
    void testReturnBook_ValidUserAndBook_ReturnsBook() {
        // Arrange
        when(databaseManager.getBooks()).thenReturn(Collections.singletonList(new Book("Book 1", "Author 1")));
        when(databaseManager.getUsers(anyList())).thenReturn(Collections.singletonList("1"));
        system.reserve("1", "1");

        // Act
        boolean result = system.returnBook("1", "1");

        // Assert
        assertTrue(result);
        assertFalse(system.getBookings().containsKey("1"));
        verify(databaseManager, times(2)).getBooks();
        verify(databaseManager, times(2)).getUsers(anyList());
    }
    
}