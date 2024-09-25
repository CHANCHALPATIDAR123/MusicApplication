package com.jukebox.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

class Registration {
    private Connection connection;
    private User loggedInUser;
    private Scanner scanner = new Scanner(System.in);

    public Registration() {
        try {
            connection = ConnectionClassForDM.getConnection(); // Establish connection to the database
            if (connection == null) {
                throw new SQLException("Failed to establish database connection.");
            }
        } catch (SQLException e) {
            System.err.println("Error initializing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("\nWelcome to Spotify Console!");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    if (loginUser()) {
                        // userMenu();
                    }
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    void registerUser() {
        String username;
        String password;

        // Username input validation
        do {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            if (username.isEmpty()) {
                System.out.println("Please enter a username.");
            }
        } while (username.isEmpty());

        // Password input validation
        do {
            System.out.print("Enter password: ");
            password = scanner.nextLine();
            if (password.isEmpty()) {
                System.out.println("Please enter a password.");
            }
        } while (password.isEmpty());

        if (connection == null) {
            System.out.println("Database connection is not available.");
            return;
        }

        try {
            String sql = "INSERT INTO Users (Username, Password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            System.out.println("User registered successfully!");
        } catch (SQLException e) {
            System.out.println("Error registering user.");
            e.printStackTrace();
        }
    }

    boolean loginUser() {
        String username;
        String password;

        // Username input validation
        do {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            if (username.isEmpty()) {
                System.out.println("Please enter a username.");
            }
        } while (username.isEmpty());

        // Password input validation
        do {
            System.out.print("Enter password: ");
            password = scanner.nextLine();
            if (password.isEmpty()) {
                System.out.println("Please enter a password.");
            }
        } while (password.isEmpty());

        if (connection == null) {
            System.out.println("Database connection is not available.");
            return false;
        }

        try {
            String sql = "SELECT UserID FROM Users WHERE Username = ? AND Password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Login successful!");
                loggedInUser = new User(resultSet.getInt("UserID"), username, password);
                return true;
            } else {
                System.out.println("Invalid username or password.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error logging in.");
            e.printStackTrace();
            return false;
        }
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}
