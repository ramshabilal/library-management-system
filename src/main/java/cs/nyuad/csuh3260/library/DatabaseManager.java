package cs.nyuad.csuh3260.library;

import java.util.ArrayList;
import java.util.List;

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

    public DatabaseManager() {
        String uri = "mongodb+srv://ramshabilal:RsRRPoY9gZCVNjhi@cluster0.siam2zv.mongodb.net/library_g3?retryWrites=true&w=majority&appName=Cluster0";
        this.mongoClient = MongoClients.create(uri);
        this.database = mongoClient.getDatabase(DATABASE_NAME);
        this.booksCollection = database.getCollection(COLLECTION_NAME);
    }

    public DatabaseManager(MongoDatabase database) {
        String uri = "mongodb+srv://ramshabilal:RsRRPoY9gZCVNjhi@cluster0.siam2zv.mongodb.net/library_g3?retryWrites=true&w=majority&appName=Cluster0";
        this.mongoClient = MongoClients.create(uri);
        this.database = database;
        this.booksCollection = database.getCollection(COLLECTION_NAME);
    }

    // Method to retrieve all books from the database
    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        // Retrieve documents from the collection
        try (MongoCursor<Document> cursor = booksCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                String id = doc.getString("id");
                String title = doc.getString("title");
                String author = doc.getString("author");
                books.add(new Book(id, title, author));
            }
        }
        return books;
    }

    public void addNewBook(Book book) {
        Document doc = new Document("id", book.getID())
                .append("title", book.getTitle())
                .append("author", book.getAuthor());
        booksCollection.insertOne(doc);
    }

    public void addMoreBooks(String bookID, Integer count) {

    }

    // Close MongoDB client
    public void close() {
        mongoClient.close();
    }

}
