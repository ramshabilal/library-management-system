package cs.nyuad.csuh3260.library;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public class DatabaseManager {
    private static final String DATABASE_NAME = "library_g3";
    private static final String COLLECTION_NAME = "books";

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> booksCollection;
    private static final String USER_COLLECTION_NAME = "users";

    private MongoCollection<Document> usersCollection;

    public DatabaseManager() {
        String uri = "mongodb+srv://ramshabilal:RsRRPoY9gZCVNjhi@cluster0.siam2zv.mongodb.net/library_g3?retryWrites=true&w=majority&appName=Cluster0";
        this.mongoClient = MongoClients.create(uri);
        this.database = mongoClient.getDatabase(DATABASE_NAME);
        this.booksCollection = database.getCollection(COLLECTION_NAME);
        this.usersCollection = database.getCollection(USER_COLLECTION_NAME);
    }

    public DatabaseManager(MongoCollection<Document> booksCollection, MongoCollection<Document> usersCollection) {
        this.booksCollection = booksCollection;
        this.usersCollection = usersCollection;
    }

    public DatabaseManager(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    // Method to retrieve all books from the database
    public List<Book> getBooks() {
        List<Book> books = new ArrayList<Book>();
        // Retrieve documents from the collection
        MongoCursor<Document> cursor = booksCollection.find().iterator();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            String id = doc.getString("id");
            String title = doc.getString("title");
            String author = doc.getString("author");
            Integer count = doc.getInteger("count");
            books.add(new Book(id, title, author, count));
        }
        return books;
    }

    public void addNewBook(Book book) {
        Document doc = new Document("id", book.getID())
                .append("title", book.getTitle())
                .append("author", book.getAuthor())
                .append("count", book.getCount());
        booksCollection.insertOne(doc);
    }

    public void addMoreBooks(String bookID, Integer count) throws Exception {
        Document book = booksCollection.find(new Document("id", bookID)).first();
        if (book != null) {
            int currentCount = book.getInteger("count");
            int newCount = currentCount + count;

            booksCollection.updateOne(
                    new Document("id", bookID),
                    new Document("$set", new Document("count", newCount)));
        } else {
            throw new Exception("No such book.");
        }
    }

    public void removeAllBook(String bookID) {
        booksCollection.deleteOne(new Document("id", bookID));
    }

    public void removeKBooks(String bookID, Integer count) throws Exception {
        Document book = booksCollection.find(new Document("id", bookID)).first();
        if (book != null) {
            int currentCount = book.getInteger("count");
            if (currentCount < count) {
                throw new Exception("There are less books to remove!");
            }
            int newCount = currentCount - count;

            booksCollection.updateOne(
                    new Document("id", bookID),
                    new Document("$set", new Document("count", newCount)));
        } else {
            throw new Exception("No such book.");
        }
    }

    public void addUser(User user) {
        Document doc = new Document("id", user.getId()).append("name", user.getName())
                .append("username", user.getUsername())
                .append("password", user.getPassword());
        usersCollection.insertOne(doc);
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        MongoCursor<Document> cursor = usersCollection.find().iterator();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            String name = doc.getString("name");
            String username = doc.getString("username");
            String password = doc.getString("password");
            users.add(new User(name, username, password));
        }
        return users;
    }

    // Close MongoDB client
    public void close() {
        mongoClient.close();
    }

}
