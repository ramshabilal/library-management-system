package cs.nyuad.csuh3260.library;

import java.util.*;

public class SystemManager {
    private DatabaseManager databaseManager;
    private Map<String, Integer> availabilityList;
    private Map<String, List<String>> bookings;
    public User curUser;

    public SystemManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.availabilityList = new HashMap<>();
        this.bookings = new HashMap<>();
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


    public boolean returnBook(String userID, String bookID) {
        
        return false;

    }

    public void addNewBook(String title, String author) {
        
    }

    public void addMoreBooks(String bookID, int count) {
        
    }

    public void removeAllBook(String bookID) {
        
    }

    public void removeKBooks(String bookID, int count) {
        
    }

    public Map<String, Integer> getAvailabilityList() {
        return availabilityList;
    }

    public Map<String, List<String>> getBookings() {
        return bookings;
    }

}
