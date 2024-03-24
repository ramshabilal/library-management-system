package cs.nyuad.csuh3260.library;

import java.util.Scanner;

public class SystemManager {

    private Scanner scanner;
    private User curUser;

    public SystemManager() {
        scanner = new Scanner(System.in);
    }

    public boolean login(String username, String password) {
        curUser = new User(username, username, password);
        return true;
    }

    public boolean signup(String name, String username, String password) {
        curUser = new User(name, username, password);
        return true;
    }

    public void run() {
        // Display greeting and instructions
        System.out.println("Hello! This is a Library program.");
        System.out.println("In order to use Library services, please signup or login.");
        System.out.println("Available methods:");
        System.out.println("Login: login <username>,<password>");
        System.out.println("Signup: signup <name>,<username>,<password>");
        System.out.println("exit: exit");

        // Keep prompting for input until the user chooses to exit or authenticates
        while (true) {
            System.out.print("Enter your command: ");
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
                    System.out.println("Invalid number of arguments. Please try again.");
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
                System.out.println("Bye!");
                scanner.close();
                return;
            } else {
                System.out.println("Invalid command. Please try again.");
            }
        }

        // Now user is authenticated
        System.out.println("Successfully authenticated! You are in. Here are available services: ");
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
        System.out.println("--- Admin side ---");
        System.out.println("addBook: addBook <title>,<author>");
        System.out.println("addMoreBooks: addMoreBooks <bookID>,<count>");
        System.out.println("search: search <keyword1>,<keyword2>,...,<keywordN>");
        System.out.println("removeAllBook: removeAllBook <bookID>");
        System.out.println("removeKBooks: removeKBooks <bookID>,<count>");
        System.out.println("exit: exit");

        // keep trying to read user input, match with available methods and call for
        // that method

        while (true) {
            // Prompt for user input
            System.out.print("Enter your command: ");
            String userInput = scanner.nextLine().trim();

            if (userInput.equals("exit")) {
                System.out.println("Bye!");
                scanner.close();
                return;
            }

            // Split the input by space to separate command and arguments
            String[] parts = userInput.split(" ", 2); // Limit split to 2 parts
            if (parts.length != 2) {
                System.out.println("Invalid command. Please try again.");
                continue;
            }
            String command = parts[0];
            String dataPart = parts[1];

            // Validate the command and call the corresponding method
            if (command.equals("addBook")) {
                // Call addBook method
                String[] data = dataPart.split(",");
                if (data.length != 2) {
                    System.out.println("Invalid command. Please try again.");
                    continue;
                }
                String title = data[0];
                String author = data[1];
                addBook(title, author);
            } else if (command.equals("addMoreBooks")) {
                // Call addMoreBooks method
                String[] data = dataPart.split(",");
                if (data.length != 2) {
                    System.out.println("Invalid command. Please try again.");
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
                    System.out.println("Invalid command. Please try again.");
                    continue;
                }
                String bookID = data[0];
                String count = data[1];
                removeKBooks(bookID, count);
            } else {
                System.out.println("Unrecognized command.");
                continue;
            }

        }
    }

    public void userProgram() {
        System.out.println("--- User side ---");
        System.out.println("search: search <keyword1>,<keyword2>,...,<keywordN>");
        System.out.println("reserve: reserve <bookID>");
        System.out.println("returnBook: returnBook <bookID>");
        System.out.println("exit: exit");

        // keep trying to read user input, match with available methods and call for
        // that method

        while (true) {
            // Prompt for user input
            System.out.print("Enter your command: ");
            String userInput = scanner.nextLine().trim();

            if (userInput.equals("exit")) {
                System.out.println("Bye!");
                scanner.close();
                return;
            }

            // Split the input by space to separate command and arguments
            String[] parts = userInput.split(" ", 2); // Limit split to 2 parts
            if (parts.length != 2) {
                System.out.println("Invalid command. Please try again.");
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
                System.out.println("Unrecognized command.");
                continue;
            }
        }
    }

    private void reserve(String bookID) {
    }

    private void returnBook(String bookID) {
    }

    public static void main(String[] args) {
        SystemManager system = new SystemManager();
        system.run();
    }

}
