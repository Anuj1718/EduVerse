package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AdminDAO {

	public void addCourse(Connection connection, Scanner scanner) {
		System.out.print("Enter course name: ");
		String courseName = scanner.nextLine();
		System.out.print("Enter description: ");
		String description = scanner.nextLine();
		System.out.print("Enter category (Technical/Non-Technical): ");
		String category = scanner.nextLine();
		System.out.print("Enter price: ");
		double price = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter instructor name: ");
		String instructorName = scanner.nextLine();

		String sql = "INSERT INTO Course (course_name, description, category, price, instructor_name) VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, courseName);
			pstmt.setString(2, description);
			pstmt.setString(3, category);
			pstmt.setDouble(4, price);
			pstmt.setString(5, instructorName);
			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Course added successfully!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	public void displayCourses(Connection connection, Scanner scanner) {

		try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM course");
				ResultSet resultSet = statement.executeQuery()) {

			System.out.println("\nAvailable Courses:");
			double price = 0;

			
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

				
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	 
	public void deleteCourse(Connection connection, Scanner scanner) {
		displayCourses( connection, scanner);
		System.out.print("Enter course ID to delete: ");
		int courseId = scanner.nextInt();
		scanner.nextLine();

		String sql = "DELETE FROM Course WHERE course_id = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, courseId);
			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Course deleted successfully!");
			} else {
				System.out.println("Course not found.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addUser(Connection connection, Scanner scanner) {
		System.out.print("Enter username: ");
		String username = scanner.nextLine();
		System.out.print("Enter email: ");
		String email = scanner.nextLine();
		System.out.print("Enter password: ");
		String password = scanner.nextLine();

		String sql = "INSERT INTO User (user_name, user_email, user_password) VALUES (?, ?, ?)";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, username);
			pstmt.setString(2, email);
			pstmt.setString(3, password);
			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("User added successfully!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void displayUser(Connection connection, Scanner scanner) {

		try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM user");
				ResultSet resultSet = statement.executeQuery()) {

			System.out.println("\nAvailable Users:");
			

			
			while (resultSet.next()) {
				int id = resultSet.getInt("user_id");
				String name = resultSet.getString("user_name");
				String email = resultSet.getString("user_email");
				String password = resultSet.getString("user_password");

				System.out.println("User ID: " + id);
				System.out.println("User Name: " + name);
				System.out.println("User Email: " + email);
				System.out.println("User Password:" + password);
				System.out.println("---------------------------------");

				
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteUser(Connection connection, Scanner scanner) {
		displayUser( connection,  scanner);
		System.out.print("Enter user ID to delete: ");
		int userId = scanner.nextInt();
		scanner.nextLine();

		String sql = "DELETE FROM User WHERE user_id = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("User deleted successfully!");
			} else {
				System.out.println("User not found.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}