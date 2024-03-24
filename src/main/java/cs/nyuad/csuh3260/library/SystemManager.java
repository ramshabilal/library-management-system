package cs.nyuad.csuh3260.library;

import java.util.*;

public class SystemManager {
    private DatabaseManager databaseManager;
    private Map<String, List<String>> bookings;
    private User curUser;
    private Map<String, Integer> availabilityList;

    public SystemManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.bookings = new HashMap<>();
        this.availabilityList = new HashMap<>();
        initializeAvailabilityList();
    }

    private void initializeAvailabilityList() {
        List<Book> allBooks = databaseManager.getBooks();
        for (Book book : allBooks) {
            availabilityList.put(book.getID(), book.getCount());
        }
    }


    //login, sign up,

    public List<Book> search(List<String> keywords) {
        
        List<Book> matchingBooks = new ArrayList<>();
        List<Book> allBooks = databaseManager.getBooks();
        for (Book book : allBooks) {
            for (String keyword : keywords) {
                if (book.getTitle().contains(keyword) || book.getAuthor().contains(keyword)) {
                    matchingBooks.add(book);
                    break;
                }
            }
        }
        return matchingBooks;
    }

    
    public boolean reserve(String bookID) {
        if (availabilityList.containsKey(bookID) && availabilityList.get(bookID) > 0) {
            // minuses 1 if available
            availabilityList.put(bookID, availabilityList.get(bookID) - 1);
            bookings.computeIfAbsent(curUser.getId(), k -> new ArrayList<>()).add(bookID);
            return true;
        }
        return false;
    }


    public boolean returnBook(String bookID) {
        // Check if the book is currently borrowed by the user
        if (bookings.containsKey(curUser.getId()) && bookings.get(curUser.getId()).contains(bookID)) {
            // Remove the book from the user's bookings
            bookings.get(curUser.getId()).remove(bookID);
            
            // Increase the availability count of the book
            int updatedCount = availabilityList.get(bookID) + 1;
            availabilityList.put(bookID, updatedCount);
            
            return true;
        }
        return false;
    }
    public boolean addNewBook(String title, String author) {
        // Check if a book with the same title and author already exists
        List<Book> allBooks = databaseManager.getBooks();
        for (Book book : allBooks) {
            if (book.getTitle().equals(title) && book.getAuthor().equals(author)) {
                return false; // Book already exists
            }
        }
        String bookId = generateBookId();
        Book book = new Book(bookId, title, author, 0);
        databaseManager.addNewBook(book);
        availabilityList.put(bookId, 0);
        return true;
    }

    public boolean addMoreBooks(String bookID, int count) {
        // Check if the book exists
        if (!availabilityList.containsKey(bookID)) {
            return false; // Book does not exist
        }
        try {
            databaseManager.addMoreBooks(bookID, count);
            availabilityList.put(bookID, availabilityList.get(bookID) + count);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
    }

    public void removeAllBook(String bookID) {
        
    }

    public void removeKBooks(String bookID, int count) {
        
    }

    private String generateBookId() {
        List<Book> allBooks = databaseManager.getBooks();
        int maxId = 0;
        for (Book book : allBooks) {
            int currentId = Integer.parseInt(book.getID());
            if (currentId > maxId) {
                maxId = currentId;
            }
        }
        return String.valueOf(maxId + 1);
    }

    public Map<String, Integer> getAvailabilityList() {
        return availabilityList;
    }

    public Map<String, List<String>> getBookings() {
        return bookings;
    }

    public void setCurUser(User curUser) {
        this.curUser = curUser;
    }

}
