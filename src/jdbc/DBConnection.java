package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/eduverse";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; 
    private static Connection connection = null;

    private DBConnection() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver"); 
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                System.err.println("JDBC Driver not found: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Failed to connect to the database: " + e.getMessage());
            }
        }
        return connection;
    }
}