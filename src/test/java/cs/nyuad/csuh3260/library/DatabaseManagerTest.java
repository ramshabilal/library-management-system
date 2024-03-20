package cs.nyuad.csuh3260.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManagerTest {

    private DatabaseManager databaseManager;

    @BeforeEach
    public void setUp() {
        databaseManager = mock(DatabaseManager.class);
    }

    @Test
    public void testGetBooks() {
        List<Book> expected = new ArrayList<>();
        expected.add(new Book("1", "Title 1", "Author 1"));
        expected.add(new Book("2", "Title 2", "Author 2"));

        when(databaseManager.getBooks()).thenReturn(expected);

        // When
        List<Book> actualBooks = databaseManager.getBooks();

        // Then
        assertNotNull(actualBooks);
        assertEquals(expected.size(), actualBooks.size());
        assertEquals(expected, actualBooks);
    }
}
