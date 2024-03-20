package cs.nyuad.csuh3260.library;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DatabaseManagerTest {

    private DatabaseManager databaseManager;
    private MongoDatabase mockDatabase;
    private MongoCollection<Document> mockBooksCollection;

    @BeforeEach
    public void setUp() {
        mockDatabase = mock(MongoDatabase.class);
        mockBooksCollection = mock(MongoCollection.class);
        when(mockDatabase.getCollection("books")).thenReturn(mockBooksCollection);
        databaseManager = new DatabaseManager(mockDatabase);
    }

    @Test
    public void testGetBooks() {
        databaseManager = mock(DatabaseManager.class);
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
    public void testAddNewBook() {
        // Given
        Book newBook = new Book("1", "New Book Title", "New Book Author");

        // When
        databaseManager.addNewBook(newBook);

        // Then
        ArgumentCaptor<Document> documentCaptor = ArgumentCaptor.forClass(Document.class);
        verify(mockBooksCollection).insertOne(documentCaptor.capture());
        Document capturedDocument = documentCaptor.getValue();
        assertEquals(newBook.getID(), capturedDocument.getString("id"));
        assertEquals(newBook.getTitle(), capturedDocument.getString("title"));
        assertEquals(newBook.getAuthor(), capturedDocument.getString("author"));
    }
}
