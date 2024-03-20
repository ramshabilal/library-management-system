package cs.nyuad.csuh3260.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cs.nyuad.csuh3260.library.exceptions.BookAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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

    @Test
    public void testAddNewBook() throws BookAlreadyExistsException {
        Book newBook = new Book("1", "New Book Title", "New Book Author");

        // Mock the behavior of getBooks() to return an empty list
        when(databaseManager.getBooks()).thenReturn(List.of());

        // When adding a new book
        databaseManager.addNewBook(newBook);

        // Then verify that the book was added successfully
        verify(databaseManager).getBooks();
        verify(databaseManager).addNewBook(newBook);
    }

    @Test
    public void testAddExistingBookShouldThrow() {
        // Given
        Book existingBook = new Book("1", "Title", "Author");

        // Mock the behavior of getBooks() to return a list containing the existing book
        when(databaseManager.getBooks()).thenReturn(List.of(existingBook));

        // When adding a new book with the same title and author
        Book newBook = new Book("2", "Title", "Author");

        // Then expect BookAlreadyExistsException to be thrown
        assertThrows(BookAlreadyExistsException.class, () -> {
            databaseManager.addNewBook(newBook);
        }, "Expected BookAlreadyExistsException but it was not thrown");
    }
}
