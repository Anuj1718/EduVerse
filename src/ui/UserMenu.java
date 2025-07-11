package ui;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import dao.UserDAO;
import dao.AdminDAO;
import jdbc.DBConnection;

public class UserMenu {
    // ANSI Color Codes
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = DBConnection.getConnection();
        UserDAO userdao = new UserDAO();
        AdminDAO admindao = new AdminDAO();

        displayEduverseDescription();

        try {
            while (true) {
                printMainMenu();
                int choice = getValidIntegerInput(scanner);
                if (choice == 0) {  
                    System.out.println("Exiting the application...");
                    break;
                }
                handleMainMenuChoice(connection, scanner, userdao, admindao, choice);
            }
        } catch (InputMismatchException e) {
            printErrorMessage("Please enter a valid number !");
            scanner.nextLine();
        } finally {
            closeConnection(connection);  
            scanner.close();
        }
    }



    private static void displayEduverseDescription() {
        System.out.println(ANSI_CYAN + "\n╔══════════════════════════════════════════════════╗");
        System.out.println("║" + ANSI_BOLD + "           Welcome to Eduverse - Your           " + ANSI_RESET + ANSI_CYAN + "  ║");
        System.out.println("║" + ANSI_BOLD + "           Gateway to Digital Learning!         " + ANSI_RESET + ANSI_CYAN + "  ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║ " + ANSI_YELLOW + "• Explore diverse courses in tech and beyond" + ANSI_CYAN + "     ║");
        System.out.println("║ " + ANSI_YELLOW + "• Learn from industry experts               " + ANSI_CYAN + "     ║");
        System.out.println("║ " + ANSI_YELLOW + "• Flexible learning paths                   " + ANSI_CYAN + "     ║");
        System.out.println("║ " + ANSI_YELLOW + "• Affordable pricing with payment options   " + ANSI_CYAN + "     ║");
        System.out.println("╚══════════════════════════════════════════════════╝" + ANSI_RESET);
    }


    private static void printMainMenu() {
        System.out.println(ANSI_CYAN + "\n╔════════════════════════════╗");
        System.out.println("║ " + ANSI_BOLD + "       MAIN MENU         " + ANSI_RESET + ANSI_CYAN + "  ║");
        System.out.println("╠════════════════════════════╣");
        System.out.println("║ " + ANSI_YELLOW + "1. User Login           " + ANSI_CYAN + "   ║");
        System.out.println("║ " + ANSI_YELLOW + "2. User Sign Up         " + ANSI_CYAN + "   ║");
        System.out.println("║ " + ANSI_YELLOW + "3. Admin Login          " + ANSI_CYAN + "   ║");
        
        System.out.println("╚════════════════════════════╝" + ANSI_RESET);
        System.out.print(ANSI_GREEN + "\n➤ Enter your choice: " + ANSI_RESET);
    }

    private static int getValidIntegerInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            printErrorMessage("Invalid input! Please enter a number.");
            scanner.next();
            System.out.print(ANSI_GREEN + "➤ Try again: " + ANSI_RESET);
        }
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }

    private static void handleMainMenuChoice(Connection connection, Scanner scanner, 
                                           UserDAO userdao, AdminDAO admindao, int choice) {
        switch (choice) {
            case 1 -> handleUserLogin(connection, scanner, userdao);
            case 2 -> handleUserSignup(connection, scanner, userdao);
            case 3 -> handleAdminLogin(connection, scanner, admindao);
            default -> printErrorMessage("Invalid choice! Please try again.");
        }
    }

    private static void handleUserLogin(Connection connection, Scanner scanner, UserDAO userdao) {
        if (userdao.loginUser(connection, scanner)) {
            System.out.print(ANSI_GREEN + "\n➤ Enter your user ID to enroll in courses: " + ANSI_RESET);
            int userId = getValidIntegerInput(scanner);
            userdao.displayCourses(connection, userId, scanner);
        } else {
            System.err.print(ANSI_BOLD +  "\nLogin failed. Please try again."); 
            handleRetryLogin(connection, scanner, userdao);
        }
    }


    private static void handleRetryLogin(Connection connection, Scanner scanner, UserDAO userdao) {
        System.out.print(ANSI_YELLOW + "\n➤ Try again? (yes/no): " + ANSI_RESET);
        String retryChoice = scanner.nextLine().toLowerCase();
        if (!retryChoice.equals("yes")) {
            printInfoMessage("Redirecting to sign-up...");
            handleUserSignup(connection, scanner, userdao);
        }
    }

    private static void handleUserSignup(Connection connection, Scanner scanner, UserDAO userdao) {
        System.out.println(ANSI_CYAN + "\n═════════════════════════════════");
        System.out.println("        NEW USER REGISTRATION");
        System.out.println("═════════════════════════════════" + ANSI_RESET);
        userdao.addUser(connection, scanner);
        printSuccessMessage("Sign-up successful! Please log in.");
    }

    private static void handleAdminLogin(Connection connection, Scanner scanner, AdminDAO admindao) {
        System.out.print(ANSI_YELLOW + "\n➤ Enter Admin Password: " + ANSI_RESET);
        String adminPassword = scanner.nextLine();
        if ("admin123".equals(adminPassword)) {
            printSuccessMessage("Authentication successful!");
            adminMenu(connection, scanner, admindao);
        } else {
            printErrorMessage("Invalid credentials!");
        }
    }

    private static void adminMenu(Connection connection, Scanner scanner, AdminDAO admindao) {
        while (true) {
            try {
                printAdminMenu();
                int adminChoice = getValidIntegerInput(scanner);
                if (adminChoice == 5) break;
                handleAdminChoice(connection, scanner, admindao, adminChoice);
            } catch (InputMismatchException e) {
                printErrorMessage("Invalid input! Please enter a number.");
                scanner.nextLine();
            }
        }
        printInfoMessage("Logging out from Admin Panel...");
    }

    private static void printAdminMenu() {
        System.out.println(ANSI_CYAN + "\n╔════════════════════════════╗");
        System.out.println("║ " + ANSI_BOLD + "     ADMIN PANEL         " + ANSI_RESET + ANSI_CYAN + "  ║");
        System.out.println("╠════════════════════════════╣");
        System.out.println("║ " + ANSI_YELLOW + "1. Add Course           " + ANSI_CYAN + "   ║");
        System.out.println("║ " + ANSI_YELLOW + "2. Delete Course        " + ANSI_CYAN + "   ║");
        System.out.println("║ " + ANSI_YELLOW + "3. Add User             " + ANSI_CYAN + "   ║");
        System.out.println("║ " + ANSI_YELLOW + "4. Delete User          " + ANSI_CYAN + "   ║");
        System.out.println("║ " + ANSI_YELLOW + "5. Logout               " + ANSI_CYAN + "   ║");
        System.out.println("╚════════════════════════════╝" + ANSI_RESET);
        System.out.print(ANSI_GREEN + "\n➤ Choose an option: " + ANSI_RESET);
    }

    private static void handleAdminChoice(Connection connection, Scanner scanner, AdminDAO admindao, int choice) {
        switch (choice) {
            case 1 -> admindao.addCourse(connection, scanner);
            case 2 -> admindao.deleteCourse(connection, scanner);
            case 3 -> admindao.addUser(connection, scanner);
            case 4 -> admindao.deleteUser(connection, scanner);
            default -> printErrorMessage("Invalid choice! Please try again.");
        }
    }

    private static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            printErrorMessage("Error closing connection: " + e.getMessage());
        }
    }

    private static void printSuccessMessage(String message) {
        System.out.println(ANSI_GREEN + "✓ " + message + ANSI_RESET);
    }

    private static void printErrorMessage(String message) {
        System.out.println(ANSI_RED + "✗ " + message + ANSI_RESET);
    }

    private static void printInfoMessage(String message) {
        System.out.println(ANSI_CYAN + "➤ " + message + ANSI_RESET);
    }
}