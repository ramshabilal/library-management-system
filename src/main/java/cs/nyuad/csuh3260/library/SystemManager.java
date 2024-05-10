package cs.nyuad.csuh3260.library;

import java.io.PrintStream;
import java.util.*;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SystemManager {
    private DatabaseManager databaseManager;
    private Map<String, List<String>> bookings;
    private User curUser;
    private Map<String, Integer> availabilityList;
    private Scanner scanner;
    private PrintStream systemOut;

    public SystemManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.bookings = new HashMap<>();
        this.availabilityList = new HashMap<>();
        initializeAvailabilityList();
        scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        systemOut = System.out;
    }

    public SystemManager(DatabaseManager databaseManager, Scanner scanner) {
        this.databaseManager = databaseManager;
        this.bookings = new HashMap<>();    
        this.availabilityList = new HashMap<>();
        initializeAvailabilityList();
        this.scanner = scanner;
        systemOut = System.out;
    }

    public SystemManager(DatabaseManager databaseManager, Scanner scanner, PrintStream systemOut) {
        this.databaseManager = databaseManager;
        this.bookings = new HashMap<>();
        this.availabilityList = new HashMap<>();
        initializeAvailabilityList();
        this.scanner = scanner;
        this.systemOut = systemOut;
    }

    public SystemManager(Scanner scanner, PrintStream systemOut, DatabaseManager databaseManager, User mockedUser) {
        this.scanner = scanner;
        this.systemOut = systemOut;
        this.databaseManager = databaseManager;
        this.curUser = mockedUser;
    }

    public SystemManager() {
        scanner = new Scanner(System.in);
        systemOut = System.out;
    }

    public SystemManager(Scanner scanner) {
        this.scanner = scanner;
        systemOut = System.out;
    }

    public SystemManager(Scanner scanner, PrintStream systemOut) {
        this.scanner = scanner;
        this.systemOut = systemOut;
    }


    private void initializeAvailabilityList() {
        List<Book> allBooks = databaseManager.getBooks();
        for (Book book : allBooks) {
            availabilityList.put(book.getID(), book.getCount());
        }
    }


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
            availabilityList.put(bookID, availabilityList.get(bookID) - 1);
            bookings.computeIfAbsent(curUser.getId(), k -> new ArrayList<>()).add(bookID);
            return true;
        }
        return false;
    }

    public boolean returnBook(String bookID) {
        if (bookings.containsKey(curUser.getId()) && bookings.get(curUser.getId()).contains(bookID)) {
            bookings.get(curUser.getId()).remove(bookID);
            int updatedCount = availabilityList.get(bookID) + 1;
            availabilityList.put(bookID, updatedCount);
            return true;
        }
        return false;
    }

    public boolean addNewBook(String title, String author) {
        List<Book> allBooks = databaseManager.getBooks();
        for (Book book : allBooks) {
            if (book.getTitle().equals(title) && book.getAuthor().equals(author)) {
                return false;
            }
        }
        String bookId = generateBookId();
        Book book = new Book(bookId, title, author, 0);
        databaseManager.addNewBook(book);
        availabilityList.put(bookId, 0);
        return true;
    }

    public boolean addMoreBooks(String bookID, int count) {
        if (!availabilityList.containsKey(bookID)) {
            return false;
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

    public boolean removeAllBook(String bookID) {
        if (!availabilityList.containsKey(bookID)) {
            return false;
        }
        databaseManager.removeAllBook(bookID);
        availabilityList.remove(bookID);
        return true;
    }

    public boolean removeKBooks(String bookID, int count) {
        // Check that book exists and has sufficient quantity
        if (!availabilityList.containsKey(bookID) || availabilityList.get(bookID) < count) {
            return false; // Book does not exist or insufficient quantity
        }
        try {
            databaseManager.removeKBooks(bookID, count);
            int availableCount = availabilityList.get(bookID);
            availabilityList.put(bookID, availableCount - count);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String generateBookId() {
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
        return new HashMap<>(availabilityList);
    }

    public Map<String, List<String>> getBookings() {
        Map<String, List<String>> bookingsCopy = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : bookings.entrySet()) {
            bookingsCopy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return bookingsCopy;
    }

    public void setCurUser(User curUser) {
        this.curUser = curUser;
    }

    public boolean login(String username, String password) {
        List<User> users = databaseManager.getUsers();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                curUser = user;
                return true;
            }
        }
        return false;
    }
    
    public boolean signup(String name, String username, String password) {
        List<User> users = databaseManager.getUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false; // User with the same username already exists
            }
        }
        User newUser = new User(name, username, password);
        databaseManager.addUser(newUser);
        curUser = newUser;
        return true;
    }

    public void run() {
        systemOut.println("Hello! This is a Library program.");
        systemOut.println("In order to use Library services, please signup or login.");
        systemOut.println("Available methods:");
        systemOut.println("Login: login <username>,<password>");
        systemOut.println("Signup: signup <name>,<username>,<password>");
        systemOut.println("exit: exit");

        while (true) {
            systemOut.print("Enter your command: ");
            String userInput = scanner.nextLine().trim();

            String[] parts = userInput.split(" ", 2);
            String command = parts[0];

            if (command.equals("login") || command.equals("signup")) {
                String[] data = parts[1].split(",", -1);
                if ((command.equals("login") && data.length != 2) ||
                        (command.equals("signup") && data.length != 3)) {
                    systemOut.println("Invalid number of arguments. Please try again.");
                    continue;
                }
                if (command.equals("login")) {
                    String username = data[0];
                    String password = data[1];
                    if (login(username, password)) {
                        break;
                    }
                } else {
                    String name = data[0];
                    String username = data[1];
                    String password = data[2];
                    if (signup(name, username, password)) {
                        break;
                    }
                }
            } else if (command.equals("exit")) {
                systemOut.println("Bye!");
                scanner.close();
                return;
            } else {
                systemOut.println("Invalid command. Please try again.");
            }
        }

        systemOut.println("Successfully authenticated! You are in. Here are available services: ");
        boolean isAdmin = curUser.isAdmin();
        if (isAdmin) {
            adminProgram();
        } else {
            userProgram();
        }
    }

    public void adminProgram() {
        systemOut.println("--- Admin side ---");
        systemOut.println("addBook: addBook <title>,<author>");
        systemOut.println("addMoreBooks: addMoreBooks <bookID>,<count>");
        systemOut.println("search: search <keyword1>,<keyword2>,...,<keywordN>");
        systemOut.println("removeAllBook: removeAllBook <bookID>");
        systemOut.println("removeKBooks: removeKBooks <bookID>,<count>");
        systemOut.println("exit: exit");

        while (true) {
            systemOut.print("Enter your command: ");
            String userInput = scanner.nextLine().trim();

            if (userInput.equals("exit")) {
                systemOut.println("Bye!");
                scanner.close();
                return;
            }

            String[] parts = userInput.split(" ", 2);
            if (parts.length != 2) {
                systemOut.println("Invalid command. Please try again.");
                continue;
            }
            String command = parts[0];
            String dataPart = parts[1];

            if (command.equals("addBook")) {
                String[] data = dataPart.split(",");
                if (data.length != 2) {
                    systemOut.println("Invalid command. Please try again.");
                    continue;
                }
                String title = data[0];
                String author = data[1];
                addNewBook(title, author);
            } else if (command.equals("addMoreBooks")) {
                String[] data = dataPart.split(",");
                if (data.length != 2) {
                    systemOut.println("Invalid command. Please try again.");
                    continue;
                }
                String bookID = data[0];
                int count = Integer.parseInt(data[1]);
                addMoreBooks(bookID, count);
            } else if (command.equals("search")) {
                String[] keywords = dataPart.split(",");
                List<Book> searchResults = search(Arrays.asList(keywords));
                systemOut.println("Search results:");
                for (Book book : searchResults) {
                    systemOut.println(book.getTitle() + " by " + book.getAuthor());
                }
            } else if (command.equals("removeAllBook")) {
                String bookID = dataPart;
                removeAllBook(bookID);
            } else if (command.equals("removeKBooks")) {
                String[] data = dataPart.split(",");
                if (data.length != 2) {
                    systemOut.println("Invalid command. Please try again.");
                    continue;
                }
                String bookID = data[0];
                int count = Integer.parseInt(data[1]);
                removeKBooks(bookID, count);
            } else {
                systemOut.println("Unrecognized command.");
            }
        }
    }

    public void userProgram() {
        systemOut.println("--- User side ---");
        systemOut.println("search: search <keyword1>,<keyword2>,...,<keywordN>");
        systemOut.println("reserve: reserve <bookID>");
        systemOut.println("returnBook: returnBook <bookID>");
        systemOut.println("exit: exit");

        while (true) {
            systemOut.print("Enter your command: ");
            String userInput = scanner.nextLine().trim();

            if (userInput.equals("exit")) {
                systemOut.println("Bye!");
                scanner.close();
                return;
            }

            String[] parts = userInput.split(" ", 2);
            if (parts.length != 2) {
                systemOut.println("Invalid command. Please try again.");
                continue;
            }
            String command = parts[0];
            String dataPart = parts[1];

            if (command.equals("search")) {
                String[] keywords = dataPart.split(",");
                List<Book> searchResults = search(Arrays.asList(keywords));
                systemOut.println("Search results:");
                for (Book book : searchResults) {
                    systemOut.println(book.getTitle() + " by " + book.getAuthor());
                }
            } else if (command.equals("reserve")) {
                String bookID = dataPart;
                boolean reserveSuccess = reserve(bookID);
                if (reserveSuccess) {
                    systemOut.println("Book reserved successfully.");
                } else {
                    systemOut.println("Failed to reserve the book.");
                }
            } else if (command.equals("returnBook")) {
                String bookID = dataPart;
                boolean returnSuccess = returnBook(bookID);
                if (returnSuccess) {
                    systemOut.println("Book returned successfully.");
                } else {
                    systemOut.println("Failed to return the book.");
                }
            } else {
                systemOut.println("Unrecognized command.");
            }
        }
    }

    public static void main(String[] args) {
        DatabaseManager databaseManager = new DatabaseManager();
        SystemManager system = new SystemManager(databaseManager);
        system.run();
    }
}