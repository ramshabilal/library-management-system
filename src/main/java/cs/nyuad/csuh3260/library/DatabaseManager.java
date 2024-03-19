package cs.nyuad.csuh3260.library;

public class DatabaseManager {

}
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class DatabaseManager {

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> users; // This is the collection for users
    private MongoCollection<Document> books; // This is the collection for books

    public DatabaseManager(String host, int port, String databaseName) {
        mongoClient = new MongoClient(host, port);
        database = mongoClient.getDatabase(databaseName);
        usersCollection = database.getCollection("users");
        booksCollection = database.getCollection("books");
    }

    // Implement methods for interacting with the database
}