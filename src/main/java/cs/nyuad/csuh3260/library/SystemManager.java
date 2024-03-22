package cs.nyuad.csuh3260.library;

import java.util.*;

public class SystemManager {

    private DatabaseManager databaseManager;
    private Map<String, Integer> availabilityList;
    private Map<String, List<String>> bookings;

    //login, sign up,

    public List<Book> search(List<String> keywords) {
        
      return null;
    }

    
    public boolean reserve(String userID, String bookID) {
        
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
