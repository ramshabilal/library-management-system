package cs.nyuad.csuh3260.library;

import cs.nyuad.csuh3260.library.Book;
import java.io.PrintStream;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



public class SystemManagerTest {

    private SystemManager system;
    private DatabaseManager databaseManager;

    @BeforeEach
    void setUp() {
        databaseManager = mock(DatabaseManager.class);
        system = new SystemManager(databaseManager);
    }

    @Test
    void testSearch_ExistingKeywords_ReturnsMatchingBooks() {
        Book book1 = new Book("1", "Book 1", "Author 1", 1);
        Book book2 = new Book("2", "Book 2", "Author 2", 1);
        List<Book> allBooks = Arrays.asList(book1, book2);
        when(databaseManager.getBooks()).thenReturn(allBooks);
    
        List<Book> result = system.search(Arrays.asList("Book"));
    
        if (result == null) {
            result = new ArrayList<Book>(0);
        }
        assertEquals(2, result.size());
        assertTrue(result.contains(book1));
        assertTrue(result.contains(book2));
        verify(databaseManager, times(2)).getBooks();
    }

    @Test
    void testReserve_ValidUserAndBook_ReservesBook() {
        Book book = new Book("1", "Book 1", "Author 1", 1);
        when(databaseManager.getBooks()).thenReturn(Collections.singletonList(book));
        
        User user = new User("1", "John", "john", "password");
        when(databaseManager.getUsers()).thenReturn(Collections.singletonList(user));
        system.setCurUser(user);
        
        system.getAvailabilityList().put("1", 1);
        
        boolean result = system.reserve("1");

        assertTrue(result);
        assertEquals(1, system.getBookings().get("1").size());
        assertEquals("1", system.getBookings().get("1").get(0));
        
    }

    @Test
    void testReturnBook_ValidUserAndBook_ReturnsBook() {
        Book book = new Book("2", "Book 1", "Author 1", 1);
        when(databaseManager.getBooks()).thenReturn(Collections.singletonList(book));
        
        User user = new User("1", "John", "john", "password");
        when(databaseManager.getUsers()).thenReturn(Collections.singletonList(user));
        system.setCurUser(user);
        
        system.getAvailabilityList().put("2", 1);
        system.reserve("2");
        
        system.returnBook("2");
        
        assertFalse(system.getBookings().containsKey("2"));
        
    }
    
    @Test
    void testAddNewBook_ValidBook_AddsBookToDatabase() {
        system.addNewBook("Book 1", "Author 1");
        
        verify(databaseManager, times(1)).addNewBook(any(Book.class));
    }
    
    @Test
    void testAddMoreBooks_ValidBookAndCount_IncreasesBookCount() {
        Book book = new Book("1", "Book 1", "Author 1", 1);
        when(databaseManager.getBooks()).thenReturn(Collections.singletonList(book));
        
        system.getAvailabilityList().put("1", 1);
        system.addMoreBooks("1", 5);
        
        Map<String, Integer> availabilityList = system.getAvailabilityList();
        assertNotNull(availabilityList);
        assertEquals(6, availabilityList.getOrDefault("1", 0));
    }

    @Test
    void testRemoveAllBook_ValidBook_RemovesBookFromDatabase() {
        Book book = new Book("1", "Book 1", "Author 1", 1);
        when(databaseManager.getBooks()).thenReturn(Collections.singletonList(book));
        
        system.removeAllBook("1");
        
        Map<String, Integer> availabilityList = system.getAvailabilityList();
        assertNotNull(availabilityList);
        assertFalse(system.getAvailabilityList().containsKey("1"));
    }

    @Test
    void testRemoveKBooks_ValidBookAndCount_DecreasesBookCount() {
        Book book = new Book("1", "Book 1", "Author 1", 1);
        when(databaseManager.getBooks()).thenReturn(Collections.singletonList(book));
        
        system.getAvailabilityList().put("1", 5); // Set the initial book count to 5
        system.removeKBooks("1", 3);
        
        Map<String, Integer> availabilityList = system.getAvailabilityList();
        assertNotNull(availabilityList);
        assertEquals(2, availabilityList.getOrDefault("1", 0));
    }
    
   
    // @Test
    // public void testSingupSuccessfully() {
    //     when(databaseManager.getUsers()).thenReturn(new ArrayList<>());

    //     boolean status = system.signup("a", "a", "a");
    //     verify(databaseManager).addUser(any());
    //     assertTrue(status);
    // }

    // @Test
    // public void testSingupWithExistingUsername() {
    //     List<User> users = List.of(new User("a", "a", "a"));
    //     when(databaseManager.getUsers()).thenReturn(users);

    //     boolean status = system.signup("a", "a", "a");
    //     assertFalse(status);
    // }

    // @Test
    // public void testLoginSuccessfully() {
    //     List<User> users = List.of(new User("a", "a", "a"));
    //     when(databaseManager.getUsers()).thenReturn(users);

    //     boolean status = system.login("a", "a");
    //     assertTrue(status);
    // }

    // @Test
    // public void testLoginFail() {
    //     List<User> users = List.of(new User("a", "a", "a"));
    //     when(databaseManager.getUsers()).thenReturn(users);

    //     boolean status = system.login("b", "b");
    //     assertFalse(status);
    // }

    @Test
    public void testAdminProgramExitCommand() {
        // Mock dependencies
        Scanner scanner = mock(Scanner.class);
        SystemManager systemManager = new SystemManager(scanner);

        // Simulate user input
        when(scanner.nextLine()).thenReturn("exit");

        // Execute method
        systemManager.adminProgram();

        // Verify behavior
        verify(scanner).close();
    }

    

    @Test
    public void testAddMoreBooksWhenAdminProgram() {
        Scanner scanner = mock(Scanner.class);
        PrintStream systemOut = mock(PrintStream.class);
        DatabaseManager databaseManager = mock(DatabaseManager.class);
        SystemManager systemManager = spy(new SystemManager(databaseManager, scanner, systemOut));

        when(databaseManager.getBooks()).thenReturn(Collections.emptyList()); // Add this line

        when(scanner.nextLine()).thenReturn("addBook 1,1")
                .thenReturn("exit");

        systemManager.adminProgram();
        verify(systemManager).addNewBook("1", "1");

        // Check for print when incorrect arguments
        scanner = mock(Scanner.class);
        systemOut = mock(PrintStream.class);
        systemManager = new SystemManager(scanner, systemOut);

        when(scanner.nextLine()).thenReturn("addMoreBooks 1,1,1")
                .thenReturn("exit");

        systemManager.adminProgram();
        verify(systemOut).println("Invalid command. Please try again.");
    }

    @Test
    public void testSearchWhenAdminProgram() {
        DatabaseManager databaseManager = mock(DatabaseManager.class);
        Scanner scanner = mock(Scanner.class);
        PrintStream systemOut = mock(PrintStream.class);
        SystemManager systemManager = spy(new SystemManager(databaseManager, scanner, systemOut));
        
        Book book1 = new Book("1", "Book 1", "Author 1", 1);
        Book book2 = new Book("2", "Book 2", "Author 2", 1);
        List<Book> allBooks = Arrays.asList(book1, book2);
        when(databaseManager.getBooks()).thenReturn(allBooks);
        
        when(scanner.nextLine()).thenReturn("search 1,2")
                .thenReturn("exit");
        
        systemManager.adminProgram();
        
        verify(systemManager).search(Arrays.asList("1", "2"));
        verify(systemOut).println("Search results:");
        verify(systemOut).println("Book 1 by Author 1");
        verify(systemOut).println("Book 2 by Author 2");
    }

    
    

    @Test
    public void testUnrecognizedCommandWhenAdminProgram() {
        Scanner scanner = mock(Scanner.class);
        PrintStream systemOut = mock(PrintStream.class);
        SystemManager systemManager = new SystemManager(scanner, systemOut);

        when(scanner.nextLine()).thenReturn("removeAllSearch 1,1,1")
                .thenReturn("exit");

        systemManager.adminProgram();
        verify(systemOut).println("Unrecognized command.");
    }

    @Test
    public void testUserProgramWithIncorrectArguments() {
        // Check for print when incorrect arguments
        Scanner scanner = mock(Scanner.class);
        PrintStream systemOut = mock(PrintStream.class);
        SystemManager systemManager = new SystemManager(scanner, systemOut);

        when(scanner.nextLine()).thenReturn("addBook")
                .thenReturn("exit");

        systemManager.userProgram();
        verify(systemOut).println("Invalid command. Please try again.");
    }

    @Test
    public void testSearchWhenUserProgram() {
        DatabaseManager databaseManager = mock(DatabaseManager.class);
        Scanner scanner = mock(Scanner.class);
        PrintStream systemOut = mock(PrintStream.class);
        SystemManager systemManager = spy(new SystemManager(databaseManager, scanner, systemOut));
        
        Book book1 = new Book("1", "Book 1", "Author 1", 1);
        Book book2 = new Book("2", "Book 2", "Author 2", 1);
        List<Book> allBooks = Arrays.asList(book1, book2);
        when(databaseManager.getBooks()).thenReturn(allBooks);
        
        when(scanner.nextLine()).thenReturn("search 1,2")
                .thenReturn("exit");
        
        systemManager.userProgram();
        
        verify(systemManager).search(Arrays.asList("1", "2"));
        verify(systemOut).println("Search results:");
        verify(systemOut).println("Book 1 by Author 1");
        verify(systemOut).println("Book 2 by Author 2");
    }
    

    @Test
    public void testReturnBookWhenUserProgram() {
        Scanner scanner = mock(Scanner.class);
        PrintStream systemOut = mock(PrintStream.class);
        DatabaseManager databaseManager = mock(DatabaseManager.class);
        User mockedUser = new User("1", "John", "john", "password");
        SystemManager systemManager = spy(new SystemManager(databaseManager, scanner, systemOut));

        systemManager.setCurUser(mockedUser); // Add this line

        Book book = new Book("1", "Book 1", "Author 1", 1);
        when(databaseManager.getBooks()).thenReturn(Collections.singletonList(book));

        when(scanner.nextLine()).thenReturn("returnBook 1")
                .thenReturn("exit");

        systemManager.userProgram();
        verify(systemManager).returnBook("1");
    }

    @Test
    public void testUnrecognizedCommandWhenUserProgram() {
        Scanner scanner = mock(Scanner.class);
        PrintStream systemOut = mock(PrintStream.class);
        SystemManager systemManager = new SystemManager(scanner, systemOut);

        when(scanner.nextLine()).thenReturn("command 1")
                .thenReturn("exit");

        systemManager.userProgram();
        verify(systemOut).println("Unrecognized command.");
    }

    @Test
    public void testLoginWhenRun() {
        Scanner scanner = mock(Scanner.class);
        PrintStream systemOut = mock(PrintStream.class);
        User mockedUser = new User("1", "1", "1");
        DatabaseManager databaseManager = mock(DatabaseManager.class);
        when(databaseManager.getUsers()).thenReturn(List.of(mockedUser));
        SystemManager systemManager = spy(new SystemManager(scanner, systemOut, databaseManager, mockedUser));

        when(scanner.nextLine()).thenReturn("login 1,1")
                .thenReturn("exit");

        when(systemManager.login("1", "1")).thenReturn(true);

        systemManager.run();
        verify(systemManager).login("1", "1");

        // Check for print when login with incorrect arguments
        scanner = mock(Scanner.class);
        systemOut = mock(PrintStream.class);
        systemManager = new SystemManager(scanner, systemOut);

        when(scanner.nextLine()).thenReturn("login 1,1,1")
                .thenReturn("exit");

        systemManager.run();
        verify(systemOut).println("Invalid number of arguments. Please try again.");
    }

    @Test
    public void testSignupWhenRun() {
        Scanner scanner = mock(Scanner.class);
        PrintStream systemOut = mock(PrintStream.class);
        User mockedUser = new User("1", "1", "1");
        DatabaseManager databaseManager = mock(DatabaseManager.class);
        when(databaseManager.getUsers()).thenReturn(List.of(mockedUser));
        SystemManager systemManager = spy(new SystemManager(scanner, systemOut, databaseManager, mockedUser));

        when(scanner.nextLine()).thenReturn("signup 1,1,1")
                .thenReturn("exit");

        when(systemManager.signup("1", "1", "1")).thenReturn(true);

        systemManager.run();
        verify(systemManager).signup("1", "1", "1");
    }

    @Test
    public void testUnrecognizedCommandWhenRun() {
        Scanner scanner = mock(Scanner.class);
        PrintStream systemOut = mock(PrintStream.class);
        SystemManager systemManager = new SystemManager(scanner, systemOut);

        when(scanner.nextLine()).thenReturn("command 1")
                .thenReturn("exit");

        systemManager.run();
        verify(systemOut).println("Invalid command. Please try again.");
    }

    @Test
    public void testAdminProgramWhenRun() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLine()).thenReturn("login admin,admin").thenReturn("exit");

        PrintStream systemOut = mock(PrintStream.class);
        DatabaseManager databaseManager = mock(DatabaseManager.class);
        User admin = new User("admin", "admin", "admin");
        when(databaseManager.getUsers()).thenReturn(List.of(admin));
        SystemManager systemManager = spy(new SystemManager(scanner, systemOut, databaseManager, admin));

        when(systemManager.login(anyString(), anyString())).thenReturn(true);

        systemManager.run();
        verify(systemManager).adminProgram();
    }

}

