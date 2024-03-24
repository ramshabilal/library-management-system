package cs.nyuad.csuh3260.library;

import cs.nyuad.csuh3260.library.Book;
import java.util.*;
import java.util.List;
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
        system = new SystemManager(databaseManager);
    }

    @Test
    void testSearch_ExistingKeywords_ReturnsMatchingBooks() {
        Book book1 = new Book("1", "Book 1", "Author 1", 1);
        Book book2 = new Book("2", "Book 2", "Author 2", 1);
        List<Book> allBooks = Arrays.asList(book1, book2);
        when(databaseManager.getBooks()).thenReturn(allBooks);
    
        List<Book> result = system.search(Arrays.asList("Book"));
    
        if (result == null) {
            result = new ArrayList<Book>(0);
        }
        assertEquals(2, result.size());
        assertTrue(result.contains(book1));
        assertTrue(result.contains(book2));
        verify(databaseManager, times(2)).getBooks();
    }

    @Test
    void testReserve_ValidUserAndBook_ReservesBook() {
        Book book = new Book("1", "Book 1", "Author 1", 1);
        when(databaseManager.getBooks()).thenReturn(Collections.singletonList(book));
        
        User user = new User("1", "John", "john", "password");
        when(databaseManager.getUsers()).thenReturn(Collections.singletonList(user));
        system.setCurUser(user);
        
        system.getAvailabilityList().put("1", 1);
        
        boolean result = system.reserve("1");

        assertTrue(result);
        assertEquals(1, system.getBookings().get("1").size());
        assertEquals("1", system.getBookings().get("1").get(0));
        
    }

    @Test
    void testReturnBook_ValidUserAndBook_ReturnsBook() {
        Book book = new Book("2", "Book 1", "Author 1", 1);
        when(databaseManager.getBooks()).thenReturn(Collections.singletonList(book));
        
        User user = new User("1", "John", "john", "password");
        when(databaseManager.getUsers()).thenReturn(Collections.singletonList(user));
        system.setCurUser(user);
        
        system.getAvailabilityList().put("2", 1);
        system.reserve("2");
        
        system.returnBook("2");
        
        assertFalse(system.getBookings().containsKey("2"));
        
    }
    
   
}