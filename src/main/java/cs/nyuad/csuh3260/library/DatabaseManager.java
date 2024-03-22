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

    // Close MongoDB client
    public void close() {
        mongoClient.close();
    }

}
