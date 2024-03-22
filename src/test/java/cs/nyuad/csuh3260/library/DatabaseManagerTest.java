package cs.nyuad.csuh3260.library;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

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
        expected.add(new Book("1", "Title 1", "Author 1", 1));
        expected.add(new Book("2", "Title 2", "Author 2", 1));

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
        Book newBook = new Book("1", "New Book Title", "New Book Author", 1);
        Document expected = new Document("id", newBook.getID())
                .append("title", newBook.getTitle())
                .append("author", newBook.getAuthor())
                .append("count", newBook.getCount());

        InsertOneResult i = mock(InsertOneResult.class);
        when(mockBooksCollection.insertOne(any(Document.class))).thenReturn(i);

        // When
        databaseManager.addNewBook(newBook);

        // Then
        verify(mockBooksCollection).insertOne(eq(expected));
    }

    @Test
    public void testAddMoreBooks() throws Exception {
        Document book = new Document("id", "1").append("title", "t").append("author", "a").append("count", 5);
        FindIterable<Document> findIterable = mock(FindIterable.class);
        when(findIterable.first()).thenReturn(book);
        when(mockBooksCollection.find(any(Bson.class))).thenReturn(findIterable);

        // Mock the updateOne() method
        UpdateResult updateResult = mock(UpdateResult.class);
        when(mockBooksCollection.updateOne(any(Bson.class), any(Bson.class))).thenReturn(updateResult);

        // Call the method under test
        databaseManager.addMoreBooks("1", 1);

        // Verify that find() was called with the correct filter
        verify(mockBooksCollection).find(eq(new Document("id", "1")));

        // Verify that updateOne() was called with the correct filter and update
        // parameters
        verify(mockBooksCollection).updateOne(
                eq(new Document("id", "1")),
                eq(new Document("$set", new Document("count", 6))));
    }

    @Test
    public void testRemoveAllBook() {
        DeleteResult mockRes = mock(DeleteResult.class);
        when(mockBooksCollection.deleteOne(any(Document.class))).thenReturn(mockRes);

        databaseManager.removeAllBook("1");

        verify(mockBooksCollection).deleteOne(eq(new Document("id", "1")));
    }

    @Test
    public void testRemoveKBooks() throws Exception {
        Document book = new Document("id", "1").append("title", "t").append("author", "a").append("count", 5);
        FindIterable<Document> findIterable = mock(FindIterable.class);
        when(findIterable.first()).thenReturn(book);
        when(mockBooksCollection.find(any(Bson.class))).thenReturn(findIterable);

        // Mock the updateOne() method
        UpdateResult updateResult = mock(UpdateResult.class);
        when(mockBooksCollection.updateOne(any(Bson.class), any(Bson.class))).thenReturn(updateResult);

        // Call the method under test
        databaseManager.removeKBooks("1", 5);

        // Verify that find() was called with the correct filter
        verify(mockBooksCollection).find(eq(new Document("id", "1")));

        // Verify that updateOne() was called with the correct filter and update
        // parameters
        verify(mockBooksCollection).updateOne(
                eq(new Document("id", "1")),
                eq(new Document("$set", new Document("count", 0))));
    }
}
