package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import java.sql.Statement;


import models.User;

public class UserDAO {
    
	public void addUser(Connection connection, Scanner scanner) {
	    try {
	        System.out.print("Enter Name: ");
	        String name = scanner.nextLine();
	        
	        System.out.print("Enter Email: ");
	        String email = scanner.nextLine();
	        
	        System.out.print("Enter Password: ");
	        String password = scanner.nextLine();
	        
	        User user = new User();
	        user.setUserName(name);
	        user.setUserEmail(email);
	        user.setUserPassword(password);
	        
	        String query = "INSERT INTO user (user_name, user_email, user_password) VALUES (?, ?, ?)";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setString(1, user.getUserName());
	        statement.setString(2, user.getUserEmail());
	        statement.setString(3, user.getUserPassword());
	        
	        int executeUpdate = statement.executeUpdate();
	        if (executeUpdate > 0) {
	            System.out.println("Welcome to Eduverse, you are now a member.");
	        } else {
	            System.out.println("Failed to insert record.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	}
    
    public boolean loginUser(Connection connection ,  Scanner scanner ) {
        
        
        try {
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();
            
            String query = "SELECT user_password , user_id , user_name FROM user WHERE user_email = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("user_password");
                int user_id = resultSet.getInt("user_id");
                String user_name = resultSet.getString("user_name");
                
                if (storedPassword.equals(password)) {
                    System.out.println("Login Successful! Welcome to Eduverse " + user_name);
                    System.out.println("your User Id is: " + user_id);
                    
                    displayEnrolledCourses(connection, user_id);
                    
                    return true;
                } else {
                    System.out.println("Incorrect Password. Please try again.");
                }
            } else {
                System.out.println("User not found. Please register first.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return false;
    }
    
    
    public int enrollInCourse(Connection connection, int userId, int courseId) {
        String enrollQuery = "INSERT INTO Enrollment (user_id, course_id, payment_status) VALUES (?, ?, ?)";

        try (PreparedStatement enrollStmt = connection.prepareStatement(enrollQuery, Statement.RETURN_GENERATED_KEYS)) {

            enrollStmt.setInt(1, userId);
            enrollStmt.setInt(2, courseId);
            enrollStmt.setString(3, "Pending"); 

            int executeUpdate = enrollStmt.executeUpdate();

            if (executeUpdate > 0) {
                System.out.println("Enrollment successful! Your payment status is Pending.");
                displayEnrolledCourses( connection,  userId);
               
                try (ResultSet generatedKeys = enrollStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); 
                    }
                }
            } else {	
                System.out.println("Failed to enroll in the course.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; 
    }
    
    public void processPayment(Connection connection, int enrollmentId, String paymentMethod, double amount) {
        String paymentQuery = "INSERT INTO Payment (enrollment_id, payment_method, amount) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(paymentQuery)) {
            
            statement.setInt(1, enrollmentId);
            statement.setString(2, paymentMethod);
            statement.setDouble(3, amount);

            int executeUpdate = statement.executeUpdate();

            if (executeUpdate > 0) {
                System.out.println("Payment successful! Enrollment status will be updated automatically...");
            } else {
                System.out.println("Payment failed. Please try again.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void displayCourses(Connection connection, int userId, Scanner scanner) {
        
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM course");
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("\nAvailable Courses for Enrollment:");
            double price = 0;

            boolean found = false;
            while (resultSet.next()) {
                int id = resultSet.getInt("course_id");
                String name = resultSet.getString("course_name");
                String description = resultSet.getString("description");
                price = resultSet.getDouble("price");

                
                System.out.println("Course ID: " + id);
                System.out.println("Course Name: " + name);
                System.out.println("Description: " + description);
                System.out.println("Course Price:" + price);
                System.out.println("---------------------------------");
                
                found = true;
            }

            if (!found) {
                System.out.println("No courses available.");
                return; 
            }

            
            
            System.out.print("Enter the Course ID to enroll in: ");
            int courseId = scanner.nextInt();
            
           
            
            int enrollmentId = enrollInCourse(connection, userId, courseId);
            if (enrollmentId != -1) {
                
            	System.out.print("Enter payment method (COD/CreditCard/DebitCard/Coupon): ");
                String paymentMethod = scanner.next();  
              
                    processPayment(connection, enrollmentId, paymentMethod, price);
            }
             else {
                System.out.println("Enrollment failed. Payment not processed.");
            }
           

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void displayEnrolledCourses(Connection connection, int userId) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT c.course_id, c.course_name, c.description, c.price " +
                "FROM course c " +
                "JOIN enrollment e ON c.course_id = e.course_id " +
                "WHERE e.user_id = ?")) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("\nCourses You Are Enrolled In:");
            boolean found = false;
            while (resultSet.next()) {
                int id = resultSet.getInt("course_id");
                String name = resultSet.getString("course_name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");

                System.out.println("---------------------------------");
                System.out.println("Course ID: " + id);
                System.out.println("Course Name: " + name);
                System.out.println("Description: " + description);
                System.out.println("Course Price: " + price);
                System.out.println("---------------------------------");
                System.out.println("Start Learning......");
                
                found = true;
            }
            
            if (!found) {
                System.out.println("You have not enrolled in any courses yet.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}