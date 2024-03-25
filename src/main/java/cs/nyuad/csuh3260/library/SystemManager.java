package cs.nyuad.csuh3260.library;

import java.util.List;
import java.io.PrintStream;
import java.util.Scanner;

public class SystemManager {

    private Scanner scanner;
    private User curUser;
    private DatabaseManager databaseManager;
    private PrintStream systemOut;

    public SystemManager() {
        scanner = new Scanner(System.in);
        databaseManager = new DatabaseManager();
        systemOut = System.out;
    }

    public SystemManager(Scanner scanner) {
        this.scanner = scanner;
        systemOut = System.out;
    }

    public SystemManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public SystemManager(Scanner scanner, PrintStream systemOut) {
        this.scanner = scanner;
        this.systemOut = systemOut;
    }

    public SystemManager(Scanner scanner, PrintStream systemOut, DatabaseManager databaseManager, User mockedUser) {
        this.scanner = scanner;
        this.systemOut = systemOut;
        this.databaseManager = databaseManager;
        this.curUser = mockedUser;
    }

    public boolean login(String username, String password) {
        // check if user exists
        List<User> users = databaseManager.getUsers();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                curUser = user;
                return true;
            }
        }
        // no such user in system
        System.out.println("User doesn't exist!");
        return false;
    }

    public boolean signup(String name, String username, String password) {
        // check if user exists
        List<User> users = databaseManager.getUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("User with same username already exists");
                return false;
            }
        }
        // create the user
        User newUser = new User(name, username, password);
        databaseManager.addUser(newUser);
        curUser = newUser;
        return true;
    }

    public void run() {
        // Display greeting and instructions
        systemOut.println("Hello! This is a Library program.");
        systemOut.println("In order to use Library services, please signup or login.");
        systemOut.println("Available methods:");
        systemOut.println("Login: login <username>,<password>");
        systemOut.println("Signup: signup <name>,<username>,<password>");
        systemOut.println("exit: exit");

        // Keep prompting for input until the user chooses to exit or authenticates
        while (true) {
            systemOut.print("Enter your command: ");
            String userInput = scanner.nextLine().trim();

            // Process user input
            // Split the input by space to separate command and arguments
            String[] parts = userInput.split(" ", 2); // Limit split to 2 parts
            String command = parts[0];

            // Validate the command and call the corresponding method
            if (command.equals("login") || command.equals("signup")) {
                // Split the data part by comma to separate username, password, and optionally
                // name for signup
                String[] data = parts[1].split(",");
                // Check if the correct number of arguments is provided
                if ((command.equals("login") && data.length != 2) ||
                        (command.equals("signup") && data.length != 3)) {
                    systemOut.println("Invalid number of arguments. Please try again.");
                    continue;
                }
                // Call the corresponding method
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

        // Now user is authenticated
        systemOut.println("Successfully authenticated! You are in. Here are available services: ");
        boolean isAdmin = curUser.isAdmin();
        if (isAdmin) {
            adminProgram();
        } else {
            userProgram();
        }
    }

    public void addBook(String title, String author) {

    }

    public void addMoreBooks(String bookID, String count) {

    }

    public void search(String keyword) {

    }

    public void removeAllBook(String bookID) {

    }

    public void removeKBooks(String bookID, String count) {

    }

    public void adminProgram() {
        systemOut.println("--- Admin side ---");
        systemOut.println("addBook: addBook <title>,<author>");
        systemOut.println("addMoreBooks: addMoreBooks <bookID>,<count>");
        systemOut.println("search: search <keyword1>,<keyword2>,...,<keywordN>");
        systemOut.println("removeAllBook: removeAllBook <bookID>");
        systemOut.println("removeKBooks: removeKBooks <bookID>,<count>");
        systemOut.println("exit: exit");

        // keep trying to read user input, match with available methods and call for
        // that method

        while (true) {
            // Prompt for user input
            systemOut.print("Enter your command: ");
            String userInput = scanner.nextLine().trim();

            if (userInput.equals("exit")) {
                systemOut.println("Bye!");
                scanner.close();
                return;
            }

            // Split the input by space to separate command and arguments
            String[] parts = userInput.split(" ", 2); // Limit split to 2 parts
            if (parts.length != 2) {
                systemOut.println("Invalid command. Please try again.");
                continue;
            }
            String command = parts[0];
            String dataPart = parts[1];

            // Validate the command and call the corresponding method
            if (command.equals("addBook")) {
                // Call addBook method
                String[] data = dataPart.split(",");
                if (data.length != 2) {
                    systemOut.println("Invalid command. Please try again.");
                    continue;
                }
                String title = data[0];
                String author = data[1];
                addBook(title, author);
            } else if (command.equals("addMoreBooks")) {
                // Call addMoreBooks method
                String[] data = dataPart.split(",");
                if (data.length != 2) {
                    systemOut.println("Invalid command. Please try again.");
                    continue;
                }
                String bookID = data[0];
                String count = data[1];
                addMoreBooks(bookID, count);
            } else if (command.equals("search")) {
                // Call search method
                String keyword = dataPart;
                search(keyword);
            } else if (command.equals("removeAllBook")) {
                // Call removeAllBook method
                String bookID = dataPart;
                removeAllBook(bookID);
            } else if (command.equals("removeKBooks")) {
                // Call removeKBooks method
                String[] data = dataPart.split(",");
                if (data.length != 2) {
                    systemOut.println("Invalid command. Please try again.");
                    continue;
                }
                String bookID = data[0];
                String count = data[1];
                removeKBooks(bookID, count);
            } else {
                systemOut.println("Unrecognized command.");
                continue;
            }

        }
    }

    public void userProgram() {
        systemOut.println("--- User side ---");
        systemOut.println("search: search <keyword1>,<keyword2>,...,<keywordN>");
        systemOut.println("reserve: reserve <bookID>");
        systemOut.println("returnBook: returnBook <bookID>");
        systemOut.println("exit: exit");

        // keep trying to read user input, match with available methods and call for
        // that method

        while (true) {
            // Prompt for user input
            systemOut.print("Enter your command: ");
            String userInput = scanner.nextLine().trim();

            if (userInput.equals("exit")) {
                systemOut.println("Bye!");
                scanner.close();
                return;
            }

            // Split the input by space to separate command and arguments
            String[] parts = userInput.split(" ", 2); // Limit split to 2 parts
            if (parts.length != 2) {
                systemOut.println("Invalid command. Please try again.");
                continue;
            }
            String command = parts[0];
            String dataPart = parts[1];

            // Validate the command and call the corresponding method
            if (command.equals("search")) {
                // Call search method
                String keyword = dataPart;
                search(keyword);
            } else if (command.equals("reserve")) {
                // Call reserve method
                String bookID = dataPart;
                reserve(bookID);
            } else if (command.equals("returnBook")) {
                // Call returnBook method
                String bookID = dataPart;
                returnBook(bookID);
            } else {
                systemOut.println("Unrecognized command.");
                continue;
            }
        }
    }

    public void reserve(String bookID) {
    }

    public void returnBook(String bookID) {
    }

    public static void main(String[] args) {
        SystemManager system = new SystemManager();
        system.run();
    }

}
