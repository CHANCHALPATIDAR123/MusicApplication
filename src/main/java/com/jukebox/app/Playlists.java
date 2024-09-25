package com.jukebox.app;

//import java.beans.Statement;
// import java.io.File;
// import java.io.FileInputStream;
// import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

class Playlists {
    Scanner scanner = new Scanner(System.in);
    Connection connection;
    User loggedInUser;
    boolean loggedIn = true;
    PreparedStatement statement;

    // Constructor to initialize the connection and user
    public Playlists(User loggedInUser) {
        this.connection = ConnectionClassForDM.getConnection(); // Get connection from the connection class
        this.loggedInUser = loggedInUser; // Pass the logged-in user

    }

    public void userMenu() {
        Album ad = new Album();
        while (loggedIn) {
            System.out.println("\n===============Home====================");
            System.out.println("1. Play songs");
            System.out.println("2. Create Playlist");
            System.out.println("3. View Playlists");
            System.out.println("4. Add Song to Playlist");
            System.out.println("5. View Songs in Playlist");
            System.out.println("6. Play Song");
            System.out.println("7. view Album");
            System.out.println("8. view Album Song");
            System.out.println("9. Play Alubm Song");
            System.out.println("10. Logout");
            System.out.println("==========================================");
            // System.out.print("Choose an option: ");
            // MainApp p = new MainApp();
            // p.getIntegerInput(scanner, null);
            // int userInput = MainApp.getIntegerInput(scanner, "Please enter a number: ");
            try {
                int choice = MainApp.getIntegerInput(scanner, "Please enter a number: ");

                switch (choice) {
                    case 1:
                        try {
                            String qry = "Select SongID,Title,Path from AllSongs";
                            statement = connection.prepareStatement(qry);
                            ResultSet rs = statement.executeQuery();
                            while (rs.next()) {
                                System.out.println(rs.getString("Title"));
                            }
                            System.out.print("Select a song name from the following path: ");
                            String name = scanner.nextLine(); // should be scanner.nextLine(), not scanner.readLine()
                            PlaySong.playSong(name); // Assuming this method is implemented in PlaySong class
                            // Playlists p=new Playlists(name);
                            PlaySong.showSongMenu();
                        } catch (Exception e) {
                            System.out.println("couldn't load the song!");
                        }
                        break;
                    case 2:
                        createPlaylist();
                        break;
                    case 3:
                        viewPlaylists();
                        break;
                    case 4:
                        addSongToPlaylist();
                        break;
                    case 5:
                        viewSongsInPlaylist();
                        break;
                    case 6:
                        playSongInPlaylist();
                        PlaySong.showSongMenu();
                        break;
                    case 7:
                        ad.viewAllAlbum();
                        break;
                    case 8: // view album songs
                        ad.viewAlbumSongs();
                        break;
                    case 9: // play songs from album
                        ad.playAlbumSong();
                        PlaySong.showSongMenu();
                        break;
                    case 10:
                        loggedIn = false;
                        System.out.println("Logged out successfully.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");

                }
            } catch (Exception e) {
                System.out.println("Enter valid number");
                System.out.println();
            }
        }
    }

    private void createPlaylist() {
        System.out.print("Enter playlist name: ");
        String playlistName = scanner.nextLine();

        try {
            String sql = "INSERT INTO Playlists (PlaylistName, UserID) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, playlistName);
            statement.setInt(2, loggedInUser.getUserID()); // Use the logged-in user's ID
            statement.executeUpdate();
            System.out.println("Playlist created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating playlist.");
            e.printStackTrace();
        }
    }

    private void viewPlaylists() {
        try {
            String sql = "SELECT * FROM Playlists WHERE UserID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, loggedInUser.getUserID()); // Use the logged-in user's ID
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int playlistId = resultSet.getInt("PlaylistID");
                String name = resultSet.getString("PlaylistName");
                System.out.println("Playlist ID: " + playlistId + ", Name: " + name);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching playlists.");
            e.printStackTrace();
        }
    }

    private void addSongToPlaylist() {
        System.out.print("Enter playlist Name: ");
        String playlistName = scanner.nextLine();
        System.out.print("Enter song name: ");
        String title = scanner.nextLine();

        try {
            // Get PlaylistID from playlist table
            String qur = "SELECT PlaylistID FROM Playlists WHERE PlaylistName = ?";
            PreparedStatement statement = connection.prepareStatement(qur);
            statement.setString(1, playlistName);
            ResultSet rs = statement.executeQuery();
            int playlistID = 0;
            if (rs.next()) {
                playlistID = rs.getInt("PlaylistID");
            }

            // Get SongID from allsongs table
            String qur1 = "SELECT SongID FROM allsongs WHERE Title = ?";
            statement = connection.prepareStatement(qur1);
            statement.setString(1, title);
            rs = statement.executeQuery();
            int songId = 0;
            if (rs.next()) {
                songId = rs.getInt("SongID");
            }

            // Insert into Playlistssongs (corrected table name)
            String sql = "INSERT INTO Playlistssongs (PlaylistID, SongID) VALUES (?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistID);
            statement.setInt(2, songId);

            statement.executeUpdate();
            System.out.println("Song added to playlist successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding song.");
            e.printStackTrace();
        }
    }

    private void viewSongsInPlaylist() {
        System.out.print("Enter playlist name: ");
        String playlistName = scanner.nextLine();

        try {
            String qur = "SELECT playlists.PlaylistName, allsongs.SongID, allsongs.Title, allsongs.Artist "
                    + "FROM playlists "
                    + "INNER JOIN Playlistssongs ON playlists.PlaylistId = Playlistssongs.PlaylistID "
                    + "INNER JOIN allsongs ON Playlistssongs.SongID = allsongs.SongID";
            PreparedStatement statement = connection.prepareStatement(qur);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                if (playlistName.equals(resultSet.getString("PlaylistName"))) {
                    int songId = resultSet.getInt("SongID");
                    String title = resultSet.getString("Title");
                    String artist = resultSet.getString("Artist");

                    System.out.println("Song ID: " + songId + " | Title: " + title + " | Artist: " + artist);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching songs.");
            e.printStackTrace();
        }
    }

    private void playSongInPlaylist() {
        System.out.print("Enter Playlist name: ");
        String playlistName = scanner.nextLine();

        try {
            String sql = "SELECT playlists.PlaylistName, playlists.PlaylistID, allsongs.SongID, allsongs.Title, allsongs.Artist, allsongs.Path "
                    + "FROM playlists "
                    + "INNER JOIN Playlistssongs ON playlists.PlaylistId = Playlistssongs.PlaylistID "
                    + "INNER JOIN allsongs ON Playlistssongs.SongID = allsongs.SongID";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                if (playlistName.equals(resultSet.getString("PlaylistName"))) {
                    int playlistId = resultSet.getInt("PlaylistID");
                    String title = resultSet.getString("Title");
                    String artist = resultSet.getString("Artist");
                    String path = resultSet.getString("Path");

                    System.out.println("PlaylistID: " + playlistId + " | Song: " + title + " | Artist: " + artist);
                    DemoMusicAudio.playSound(path); // Assuming you have this method for playing the song
                } else {
                    System.out.println("Song not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching song.");
            e.printStackTrace();
        }
    }
}
