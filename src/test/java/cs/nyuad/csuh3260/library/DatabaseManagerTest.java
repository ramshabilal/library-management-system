package cs.nyuad.csuh3260.library;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.print.Doc;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

public class DatabaseManagerTest {

    private DatabaseManager databaseManager;
    private MongoCollection<Document> mockBooksCollection;
    private MongoCollection<Document> mockUsersCollection;

    @BeforeEach
    public void setUp() {
        mockBooksCollection = mock(MongoCollection.class);
        mockUsersCollection = mock(MongoCollection.class);
        databaseManager = new DatabaseManager(mockBooksCollection, mockUsersCollection);
    }

    public Document convertBookToDocument(Book book) {
        Document doc = new Document("id", book.getID())
                .append("title", book.getTitle())
                .append("author", book.getAuthor())
                .append("count", book.getCount());
        return doc;
    }

    public Document convertUserToDocument(User user) {
        Document doc = new Document("name", user.getName())
                .append("username", user.getUsername())
                .append("password", user.getPassword());
        return doc;
    }

    @Test
    public void testGetBooks() {
        mockBooksCollection = mock(MongoCollection.class);
        FindIterable<Document> findIterable = mock(FindIterable.class);
        MongoCursor<Document> iterator = mock(MongoCursor.class);
        Book book1 = new Book("1", "1", "1", 1);
        Book book2 = new Book("2", "2", "2", 2);
        List<Document> expectedBooks = new ArrayList<Document>();
        expectedBooks.add(convertBookToDocument(book1));
        expectedBooks.add(convertBookToDocument(book2));

        when(iterator.hasNext())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);
        when(iterator.next())
                .thenReturn(expectedBooks.get(0))
                .thenReturn(expectedBooks.get(1));
        when(findIterable.iterator()).thenReturn(iterator);
        when(mockBooksCollection.find()).thenReturn(findIterable);
        mockUsersCollection = mock(MongoCollection.class);
        databaseManager = new DatabaseManager(mockBooksCollection, mockUsersCollection);

        List<Book> actual = databaseManager.getBooks();
        assertEquals(actual.size(), 2);
        for (int i = 0; i < expectedBooks.size(); i++) {
            assertEquals(expectedBooks.get(i), convertBookToDocument(actual.get(i)));
        }
    }

    @Test
    public void testAddNewBook() {
        mockBooksCollection = mock(MongoCollection.class);
        mockUsersCollection = mock(MongoCollection.class);
        databaseManager = new DatabaseManager(mockBooksCollection, mockUsersCollection);
        // Given
        Book newBook = new Book("1", "New Book Title", "New Book Author", 1);
        Document expected = new Document("id", newBook.getID())
                .append("title", newBook.getTitle())
                .append("author", newBook.getAuthor())
                .append("count", newBook.getCount());

        InsertOneResult i = mock(InsertOneResult.class);
        Document doc = new Document("id", newBook.getID())
                .append("title", newBook.getTitle())
                .append("author", newBook.getAuthor())
                .append("count", newBook.getCount());
        when(mockBooksCollection.insertOne(doc)).thenReturn(i);

        // When
        databaseManager.addNewBook(newBook);

        // Then
        verify(mockBooksCollection).insertOne(eq(expected));
    }

    @Test
    public void testAddMoreBooks() throws Exception {
        mockBooksCollection = mock(MongoCollection.class);
        mockUsersCollection = mock(MongoCollection.class);
        databaseManager = new DatabaseManager(mockBooksCollection, mockUsersCollection);
        assertNotNull(mockBooksCollection);
        Document book = new Document("id", "1").append("title", "t").append("author", "a").append("count", 5);
        FindIterable<Document> findIterable = mock(FindIterable.class);
        when(findIterable.first()).thenReturn(book);
        when(mockBooksCollection.find(new Document("id", "1"))).thenReturn(findIterable);

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
    public void testAddMoreBooksWhenNoBook() throws Exception {
        mockBooksCollection = mock(MongoCollection.class);
        mockUsersCollection = mock(MongoCollection.class);
        databaseManager = new DatabaseManager(mockBooksCollection, mockUsersCollection);

        FindIterable<Document> findIterable = mock(FindIterable.class);
        when(findIterable.first()).thenReturn(null);
        when(mockBooksCollection.find(new Document("id", "1"))).thenReturn(findIterable);

        // Call the method under test
        assertThrows(Exception.class, () -> {
            databaseManager.addMoreBooks("1", 1);
        });
    }

    @Test
    public void testRemoveAllBook() {
        mockBooksCollection = mock(MongoCollection.class);
        mockUsersCollection = mock(MongoCollection.class);
        databaseManager = new DatabaseManager(mockBooksCollection, mockUsersCollection);
        assertNotNull(mockBooksCollection);
        DeleteResult mockRes = mock(DeleteResult.class);
        when(mockBooksCollection.deleteOne(new Document("id", "1"))).thenReturn(mockRes);

        databaseManager.removeAllBook("1");

        verify(mockBooksCollection).deleteOne(eq(new Document("id", "1")));
    }

    @Test
    public void testRemoveKBooks() throws Exception {
        mockBooksCollection = mock(MongoCollection.class);
        mockUsersCollection = mock(MongoCollection.class);
        databaseManager = new DatabaseManager(mockBooksCollection, mockUsersCollection);
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

    @Test
    public void testRemoveKMoreBooksThanCount() throws Exception {
        mockBooksCollection = mock(MongoCollection.class);
        mockUsersCollection = mock(MongoCollection.class);
        databaseManager = new DatabaseManager(mockBooksCollection, mockUsersCollection);
        Document book = new Document("id", "1")
                .append("title", "t")
                .append("author", "a")
                .append("count", 5);
        FindIterable<Document> findIterable = mock(FindIterable.class);
        when(findIterable.first()).thenReturn(book);
        when(mockBooksCollection.find(any(Bson.class))).thenReturn(findIterable);

        // Mock the updateOne() method
        UpdateResult updateResult = mock(UpdateResult.class);
        when(mockBooksCollection.updateOne(any(Bson.class), any(Bson.class))).thenReturn(updateResult);

        // Call the method under test
        assertThrows(Exception.class, () -> {
            databaseManager.removeKBooks("1", 6);
        });
    }

    @Test
    public void testRemoveKMoreBooksWhenNoBook() throws Exception {
        mockBooksCollection = mock(MongoCollection.class);
        mockUsersCollection = mock(MongoCollection.class);
        databaseManager = new DatabaseManager(mockBooksCollection, mockUsersCollection);

        FindIterable<Document> findIterable = mock(FindIterable.class);
        when(findIterable.first()).thenReturn(null);
        when(mockBooksCollection.find(any(Bson.class))).thenReturn(findIterable);

        // Call the method under test
        assertThrows(Exception.class, () -> {
            databaseManager.removeKBooks("1", 5);
        });
    }

    @Test
    public void testAddUser() {
        mockBooksCollection = mock(MongoCollection.class);
        mockUsersCollection = mock(MongoCollection.class);
        databaseManager = new DatabaseManager(mockBooksCollection, mockUsersCollection);
        // Given
        String id = UUID.randomUUID().toString();
        User user = new User(id, "John Doe", "johndoe", "password123");
        Document expected = new Document("id", id).append("name", "John Doe").append("username", "johndoe")
                .append("password", "password123");

        InsertOneResult a = mock(InsertOneResult.class);
        Document doc = new Document("id", user.getId())
                .append("name", user.getName())
                .append("username", user.getUsername())
                .append("password", user.getPassword());
        when(mockUsersCollection.insertOne(doc)).thenReturn(a);

        // When
        databaseManager.addUser(user);

        // Then
        verify(mockUsersCollection).insertOne(eq(expected));
    }

    @Test
    public void testGetUsers() {
        mockBooksCollection = mock(MongoCollection.class);
        mockUsersCollection = mock(MongoCollection.class);
        FindIterable<Document> findIterable = mock(FindIterable.class);
        MongoCursor<Document> iterator = mock(MongoCursor.class);
        User user1 = new User("1", "1", "1");
        User user2 = new User("2", "2", "2");
        List<Document> expectedUsers = new ArrayList<Document>();
        expectedUsers.add(convertUserToDocument(user1));
        expectedUsers.add(convertUserToDocument(user2));

        when(iterator.hasNext())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);
        when(iterator.next())
                .thenReturn(expectedUsers.get(0))
                .thenReturn(expectedUsers.get(1));
        when(findIterable.iterator()).thenReturn(iterator);
        when(mockUsersCollection.find()).thenReturn(findIterable);
        databaseManager = new DatabaseManager(mockBooksCollection, mockUsersCollection);

        List<User> actual = databaseManager.getUsers();

        assertEquals(actual.size(), 2);
        for (int i = 0; i < expectedUsers.size(); i++) {
            assertEquals(expectedUsers.get(i), convertUserToDocument(actual.get(i)));
        }
    }

    @Test
    public void testCose() {
        MongoClient client = mock(MongoClient.class);
        databaseManager = new DatabaseManager(client);

        databaseManager.close();

        verify(client).close();
    }
}
