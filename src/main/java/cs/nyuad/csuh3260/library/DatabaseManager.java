package cs.nyuad.csuh3260.library;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.internal.bulk.DeleteRequest;

import org.bson.Document;

public class DatabaseManager {

    private static final String DATABASE_NAME = "library_g3";
    private static final String USER_COLLECTION_NAME = "users";

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> usersCollection;

    public DatabaseManager() {
        String uri = "mongodb+srv://ramshabilal:RsRRPoY9gZCVNjhi@cluster0.siam2zv.mongodb.net/library_g3?retryWrites=true&w=majority&appName=Cluster0";
        this.mongoClient = MongoClients.create(uri);
        this.database = mongoClient.getDatabase(DATABASE_NAME);
        this.usersCollection = database.getCollection(USER_COLLECTION_NAME);
    }

    public DatabaseManager(MongoDatabase database) {
        String uri = "mongodb+srv://ramshabilal:RsRRPoY9gZCVNjhi@cluster0.siam2zv.mongodb.net/library_g3?retryWrites=true&w=majority&appName=Cluster0";
        this.mongoClient = MongoClients.create(uri);
        this.database = database;
        this.usersCollection = database.getCollection(USER_COLLECTION_NAME);
    }

    public void addUser(User user) {
        Document doc = new Document("id", user.getId()).append("name", user.getName()).append("username", user.getUsername())
                .append("password", user.getPassword());
        usersCollection.insertOne(doc);
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        MongoCursor<Document> cursor = usersCollection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                users.add(new User(doc.getString("name"), doc.getString("username"), doc.getString("password")));
            }
        } finally {
            cursor.close();
        }
        return users;
    }

    // Close MongoDB client
    public void close() {
        mongoClient.close();
    }

}
