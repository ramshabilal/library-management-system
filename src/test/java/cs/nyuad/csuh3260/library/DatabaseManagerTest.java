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
import java.util.UUID;

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
    private MongoCollection<Document> mockUsersCollection;

    @BeforeEach
    public void setUp() {
        mockDatabase = mock(MongoDatabase.class);
        mockUsersCollection = mock(MongoCollection.class);
        when(mockDatabase.getCollection("users")).thenReturn(mockUsersCollection);
        databaseManager = new DatabaseManager(mockDatabase);
    }

    @Test
    public void testGetUsers() {
        databaseManager = mock(DatabaseManager.class);
        List<User> expected = new ArrayList<>();
        expected.add(new User("John Doe", "johndoe", "password123"));
        expected.add(new User("Jane Smith", "janesmith", "password456"));

        when(databaseManager.getUsers()).thenReturn(expected);

        // When
        List<User> actualUsers = databaseManager.getUsers();

        // Then
        assertNotNull(actualUsers);
        assertEquals(expected, actualUsers);
        assertEquals(expected.size(), actualUsers.size());
    }

    @Test
    public void testAddUser() {
        // Given
        String id = UUID.randomUUID().toString();
        User user = new User(id, "John Doe", "johndoe", "password123");
        Document expected = new Document("id", id).append("name", "John Doe").append("username", "johndoe").append("password", "password123");

        InsertOneResult a = mock(InsertOneResult.class);
        when(mockUsersCollection.insertOne(any(Document.class))).thenReturn(a);

        // When
        databaseManager.addUser(user);

        // Then
        verify(mockUsersCollection).insertOne(eq(expected));
    }
}
