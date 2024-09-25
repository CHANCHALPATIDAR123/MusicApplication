package com.jukebox.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class PlaySong {
    static Connection con = ConnectionClassForDM.getConnection();
    static List<String> songPaths = new ArrayList<>();
    static int currentSongIndex = -1; // Keep track of the current song's index

    // Load all songs from the database
    public static boolean loadSongs() {
        if (con == null) {
            System.out.println("Failed to establish a database connection.");
            return false;
        }

        String sql = "SELECT Path FROM AllSongs";
        try (PreparedStatement st = con.prepareStatement(sql);
                ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                String songPath = rs.getString("Path");
                if (songPath != null && !songPath.isEmpty()) {
                    songPaths.add(songPath); // Add song path to list
                }
            }

            if (!songPaths.isEmpty()) {
                System.out.println("Songs loaded successfully.");
                return true;
            } else {
                System.out.println("No songs found in the database.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQL error while loading songs: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Play the current song based on the index
    public static boolean playCurrentSong() {
        if (currentSongIndex >= 0 && currentSongIndex < songPaths.size()) {
            String songPath = songPaths.get(currentSongIndex);
            DemoMusicAudio.playSound(songPath); // Play the song
            return true;
        }
        System.out.println("No song is currently selected.");
        return false;
    }

    // Play the next song
    public static boolean playNextSong() {
        if (currentSongIndex + 1 < songPaths.size()) {
            currentSongIndex++; // Move to next song
            return playCurrentSong();
        } else {
            System.out.println("You're at the last song.");
            return false;
        }
    }

    // Play the previous song
    public static boolean playPreviousSong() {
        if (currentSongIndex - 1 >= 0) {
            currentSongIndex--; // Move to previous song
            return playCurrentSong();
        } else {
            System.out.println("You're at the first song.");
            return false;
        }
    }

    // Method to play a song by title (initial load)
    public static boolean playSong(String songName) {
        if (loadSongs()) { // Load all songs first
            String sql = "SELECT Path FROM AllSongs WHERE Title = ?";
            try (PreparedStatement st = con.prepareStatement(sql)) {
                st.setString(1, songName); // Set the song name in the query
                ResultSet rs = st.executeQuery();

                if (rs.next()) { // Check if a song was found
                    String songPath = rs.getString("Path");
                    currentSongIndex = songPaths.indexOf(songPath); // Set the current song index
                    playCurrentSong(); // Play the song
                    showSongMenu(); // Show options for next, previous, or quit
                    return true;
                } else {
                    System.out.println("Song not found in the database.");
                }
            } catch (SQLException e) {
                System.out.println("SQL error while playing song: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return false; // Return false if song is not found or an exception occurs
    }

    // Method to show the menu while the song is playing
    public static void showSongMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Options: (N)ext, (P)revious, (Q)uit");
            String input = scanner.nextLine().toUpperCase();

            switch (input) {
                case "N":
                    playNextSong();
                    break;
                case "P":
                    playPreviousSong();
                    break;
                case "Q":
                    System.out.println("Stopping playback.");
                    return; // Exit the loop
                default:
                    System.out.println("Invalid input. Please press N, P, or Q.");
                    break;
            }
        }
    }
}
