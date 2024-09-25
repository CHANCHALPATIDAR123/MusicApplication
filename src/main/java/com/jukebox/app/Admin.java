package com.jukebox.app;

import java.sql.*;
import java.util.Scanner;

public class Admin {
    private Connection connection;
    private Scanner scanner = new Scanner(System.in);
    private int adminID; // Variable to store admin's ID after login

    public Admin() {
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

    public boolean loginAdmin() {
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        if (connection == null) {
            System.out.println("Database connection is not available.");
            return false;
        }

        try {
            String sql = "SELECT AdminID FROM Admins WHERE Username = ? AND Password = ? AND isAdmin = 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                adminID = resultSet.getInt("AdminID"); // Get the admin ID
                System.out.println("Admin login successful!");
                return true;
            } else {
                System.out.println("Invalid admin credentials.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error logging in admin.");
            e.printStackTrace();
            return false;
        }
    }

    public void accessAdminFeatures() {
        if (loginAdmin()) {
            manageSongs(); // Access admin functions after successful login
        } else {
            System.out.println("Access denied. Admin credentials required.");
        }
    }

    public void manageSongs() {
        boolean running = true;
        while (running) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add Song");
            System.out.println("2. Delete Song");
            System.out.println("3. View All Songs");
            System.out.println("4. Create Album");
            System.out.println("5. Add Song to Album");
            System.out.println("6. View Your Albums");
            System.out.println("7. View Your Album Songs");
            System.out.println("8. Play Song from Album");
            System.out.println("9. Exit");
            int choice = MainApp.getIntegerInput(scanner, "Please enter a number: ");
            // System.out.println("Enter choice:");
            // int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine()); // Handle input safely
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    addSong();
                    break;
                case 2:
                    System.out.print("Enter song ID to delete: ");
                    int songID = Integer.parseInt(scanner.nextLine());
                    deleteSong(songID);
                    break;
                case 3:
                    viewAllSongs();
                    break;
                case 4:
                    createAlbum();
                    break;
                case 5:
                    addSongToAlbum();
                    break;
                case 6:
                    viewYourAlbums();
                    break;
                case 7:
                    viewYourAlbumSongs();
                    break;
                case 8:
                    playYourAlbumSong();
                    PlaySong.showSongMenu();
                    break;
                case 9:
                    running = false; // Exit the loop
                    System.out.println("Exiting Admin Menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addSong() {
        System.out.print("Enter song title: ");
        String title = scanner.nextLine();
        System.out.print("Enter artist: ");
        String artist = scanner.nextLine();
        System.out.print("Enter file path: ");
        String path = scanner.nextLine();

        if (connection == null) {
            System.out.println("Database connection is not available.");
            return;
        }

        try {
            Song.AddSongToDB(title, artist, path); // Call static method from Song class
        } catch (Exception e) {
            System.out.println("Error adding song.");
            e.printStackTrace();
        }
    }

    public void deleteSong(int songID) {
        if (connection == null) {
            System.out.println("Database connection is not available.");
            return;
        }

        try {
            // Step 1: Delete from playlistssongs first
            String sqlDeleteFromPlaylists = "DELETE FROM playlistssongs WHERE SongID = ?";
            try (PreparedStatement pstmtDeleteFromPlaylists = connection.prepareStatement(sqlDeleteFromPlaylists)) {
                pstmtDeleteFromPlaylists.setInt(1, songID);
                pstmtDeleteFromPlaylists.executeUpdate();
            }

            // Step 2: Delete from allsongs after deleting references
            String sqlDeleteFromAllSongs = "DELETE FROM allsongs WHERE SongID = ?";
            try (PreparedStatement pstmtDeleteFromAllSongs = connection.prepareStatement(sqlDeleteFromAllSongs)) {
                pstmtDeleteFromAllSongs.setInt(1, songID);
                int affectedRows = pstmtDeleteFromAllSongs.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Song deleted successfully.");
                } else {
                    System.out.println("Song not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error deleting song: " + e.getMessage());
        }
    }

    private void viewAllSongs() {
        if (connection == null) {
            System.out.println("Database connection is not available.");
            return;
        }

        try {
            Song.getAllSongs(); // Call static method from Song class
        } catch (Exception e) {
            System.out.println("Error retrieving songs.");
            e.printStackTrace();
        }
    }

    private void createAlbum() {
        System.out.print("Enter Album Name: ");
        String albumName = scanner.nextLine();

        try {
            String sql = "INSERT INTO Album (AlbumName, AdminID) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, albumName);
            statement.setInt(2, adminID); // Use the logged-in admin's ID
            statement.executeUpdate();
            System.out.println("Album created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating album.");
            e.printStackTrace();
        }
    }

    public void viewYourAlbums() {
        try {
            String sql = "SELECT AlbumID, AlbumName FROM Album WHERE AdminID = ?";
            PreparedStatement stat = connection.prepareStatement(sql);
            stat.setInt(1, adminID); // Use the logged-in admin's ID
            ResultSet resultSet = stat.executeQuery();

            while (resultSet.next()) {
                int albumID = resultSet.getInt("AlbumID");
                String name = resultSet.getString("AlbumName");
                System.out.println("Album ID: " + albumID + " | Name: " + name);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching albums.");
            e.printStackTrace();
        }
    }

    private void addSongToAlbum() {
        System.out.print("Enter Album Name: ");
        String albumName = scanner.nextLine();
        System.out.print("Enter song name: ");
        String title = scanner.nextLine();

        try {
            // Get AlbumID from Album table
            String qur = "SELECT AlbumID FROM Album WHERE AlbumName = ?";
            PreparedStatement statement = connection.prepareStatement(qur);
            statement.setString(1, albumName);
            ResultSet rs = statement.executeQuery();

            int albumID = 0;
            if (rs.next()) {
                albumID = rs.getInt("AlbumID");
            } else {
                System.out.println("Album not found.");
                return;
            }

            // Get SongID from allsongs table
            String qur1 = "SELECT SongID FROM allsongs WHERE Title = ?";
            statement = connection.prepareStatement(qur1);
            statement.setString(1, title);
            rs = statement.executeQuery();

            int songID = 0;
            if (rs.next()) {
                songID = rs.getInt("SongID");
            } else {
                System.out.println("Song not found.");
                return;
            }

            // Insert song into the album
            String sql = "INSERT INTO AlbumSongs (AlbumID, SongID) VALUES (?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, albumID);
            statement.setInt(2, songID);
            statement.executeUpdate();
            System.out.println("Song added to Album successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding song.");
            e.printStackTrace();
        }
    }

    public void viewYourAlbumSongs() {
        System.out.print("Enter Album name: ");
        String albumName = scanner.nextLine();

        try {
            String qur = "SELECT Album.AlbumName, allsongs.SongID, allsongs.Title, allsongs.Artist, allsongs.Path "
                    + "FROM allsongs "
                    + "INNER JOIN AlbumSongs ON allsongs.SongID = AlbumSongs.SongID "
                    + "INNER JOIN Album ON AlbumSongs.AlbumID = Album.AlbumID "
                    + "WHERE Album.AdminID = ? AND Album.AlbumName = ?";
            PreparedStatement statement = connection.prepareStatement(qur);
            statement.setInt(1, adminID); // Use the logged-in admin's ID
            statement.setString(2, albumName);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int songID = rs.getInt("SongID");
                String title = rs.getString("Title");
                String artist = rs.getString("Artist");
                String path = rs.getString("Path");
                System.out.println(
                        "Song ID: " + songID + " | Title: " + title + " | Artist: " + artist + " | Path: " + path);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching songs.");
            e.printStackTrace();
        }
    }

    public void playYourAlbumSong() {
        System.out.print("Enter Album Name: ");
        String albumName = scanner.nextLine();

        try {
            // Correct the SQL query and add a WHERE clause to filter by the provided album
            // name
            String sql = "SELECT Album.AlbumID, allsongs.SongID, allsongs.Title, allsongs.Artist, allsongs.Path " +
                    "FROM Album " +
                    "INNER JOIN AlbumSongs ON Album.AlbumID = AlbumSongs.AlbumID " +
                    "INNER JOIN allsongs ON AlbumSongs.SongID = allsongs.SongID " +
                    "WHERE Album.AlbumName = ? AND Album.AdminID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, albumName); // Set album name from user input
            statement.setInt(2, adminID); // Use the logged-in admin's ID
            ResultSet resultSet = statement.executeQuery();

            boolean albumFound = false; // To track if any songs were found

            // Process each song in the album
            while (resultSet.next()) {
                albumFound = true;
                int albumID = resultSet.getInt("AlbumID");
                String title = resultSet.getString("Title");
                String artist = resultSet.getString("Artist");
                String path = resultSet.getString("Path");

                System.out.println("Album ID: " + albumID + " | Song: " + title + " | Artist: " + artist);

                // Play the song using DemoMusicAudio
                DemoMusicAudio.playSound(path);
            }

            if (!albumFound) {
                System.out.println("No songs found for the specified album.");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching songs from the album.");
            e.printStackTrace();
        }
    }

}
