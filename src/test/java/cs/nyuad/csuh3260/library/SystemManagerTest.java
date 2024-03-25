package cs.nyuad.csuh3260.library;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

public class SystemManagerTest {

    private SystemManager systemManager;
    private DatabaseManager dbManager;

    private void setup() {
        dbManager = mock(DatabaseManager.class);
        systemManager = new SystemManager(dbManager);
    }

    @Test
    public void testSingupSuccessfully() {
        setup();
        when(dbManager.getUsers()).thenReturn(new ArrayList<>());

        boolean status = systemManager.signup("a", "a", "a");
        verify(dbManager).addUser(any());
        assertTrue(status);
    }

    @Test
    public void testSingupWithExistingUsername() {
        setup();
        List<User> users = List.of(new User("a", "a", "a"));
        when(dbManager.getUsers()).thenReturn(users);

        boolean status = systemManager.signup("a", "a", "a");
        assertFalse(status);
    }

    @Test
    public void testLoginSuccessfully() {
        setup();
        List<User> users = List.of(new User("a", "a", "a"));
        when(dbManager.getUsers()).thenReturn(users);

        boolean status = systemManager.login("a", "a");
        assertTrue(status);
    }

    @Test
    public void testLoginFail() {
        setup();
        List<User> users = List.of(new User("a", "a", "a"));
        when(dbManager.getUsers()).thenReturn(users);

        boolean status = systemManager.login("b", "b");
        assertFalse(status);
    }

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
    public void testAddBookWhenAdminProgram() {
        Scanner scanner = mock(Scanner.class);
        PrintStream systemOut = mock(PrintStream.class);
        SystemManager systemManager = spy(new SystemManager(scanner, systemOut));

        when(scanner.nextLine()).thenReturn("addBook 1,1")
                .thenReturn("exit");

        systemManager.adminProgram();
        verify(systemManager).addBook("1", "1");

        // Check for print when incorrect arguments
        scanner = mock(Scanner.class);
        systemOut = mock(PrintStream.class);
        systemManager = new SystemManager(scanner, systemOut);

        when(scanner.nextLine()).thenReturn("addBook 1,1,1")
                .thenReturn("exit");

        systemManager.adminProgram();
        verify(systemOut).println("Invalid command. Please try again.");
    }

    @Test
    public void testAddMoreBooksWhenAdminProgram() {
        Scanner scanner = mock(Scanner.class);
        PrintStream systemOut = mock(PrintStream.class);
        SystemManager systemManager = spy(new SystemManager(scanner, systemOut));

        when(scanner.nextLine()).thenReturn("addMoreBooks 1,1")
                .thenReturn("exit");

        systemManager.adminProgram();
        verify(systemManager).addMoreBooks("1", "1");

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
        Scanner scanner = mock(Scanner.class);
        PrintStream systemOut = mock(PrintStream.class);
        SystemManager systemManager = spy(new SystemManager(scanner, systemOut));

        when(scanner.nextLine()).thenReturn("search 1")
                .thenReturn("exit");

        systemManager.adminProgram();
        verify(systemManager).search("1");
    }

    @Test
    public void testRemoveAllBookWhenAdminProgram() {
        Scanner scanner = mock(Scanner.class);
        PrintStream systemOut = mock(PrintStream.class);
        SystemManager systemManager = spy(new SystemManager(scanner, systemOut));

        when(scanner.nextLine()).thenReturn("removeAllBook 1")
                .thenReturn("exit");

        systemManager.adminProgram();
        verify(systemManager).removeAllBook("1");
    }

    @Test
    public void testRemoveKBooksWhenAdminProgram() {
        Scanner scanner = mock(Scanner.class);
        PrintStream systemOut = mock(PrintStream.class);
        SystemManager systemManager = spy(new SystemManager(scanner, systemOut));

        when(scanner.nextLine()).thenReturn("removeKBooks 1,1")
                .thenReturn("exit");

        systemManager.adminProgram();
        verify(systemManager).removeKBooks("1", "1");

        // Check for print when incorrect arguments
        scanner = mock(Scanner.class);
        systemOut = mock(PrintStream.class);
        systemManager = new SystemManager(scanner, systemOut);

        when(scanner.nextLine()).thenReturn("removeKBooks 1,1,1")
                .thenReturn("exit");

        systemManager.adminProgram();
        verify(systemOut).println("Invalid command. Please try again.");
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
        Scanner scanner = mock(Scanner.class);
        PrintStream systemOut = mock(PrintStream.class);
        SystemManager systemManager = spy(new SystemManager(scanner, systemOut));

        when(scanner.nextLine()).thenReturn("search 1")
                .thenReturn("exit");

        systemManager.userProgram();
        verify(systemManager).search("1");
    }

    @Test
    public void testReserveWhenUserProgram() {
        Scanner scanner = mock(Scanner.class);
        PrintStream systemOut = mock(PrintStream.class);
        SystemManager systemManager = spy(new SystemManager(scanner, systemOut));

        when(scanner.nextLine()).thenReturn("reserve 1")
                .thenReturn("exit");

        systemManager.userProgram();
        verify(systemManager).reserve("1");
    }

    @Test
    public void testReturnBookWhenUserProgram() {
        Scanner scanner = mock(Scanner.class);
        PrintStream systemOut = mock(PrintStream.class);
        SystemManager systemManager = spy(new SystemManager(scanner, systemOut));

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
