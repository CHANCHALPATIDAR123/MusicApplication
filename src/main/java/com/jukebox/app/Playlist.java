// package com.jukebox.app;

// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Scanner;

// class PlaySong {
// static Connection con = ConnectionClassForDM.getConnection();
// private static List<String> songPaths = new ArrayList<>();
// private static List<String> songTitles = new ArrayList<>();
// private static int currentSongIndex = -1; // Track the current song
// private static boolean isSongPlaying = false; // Track if a song is currently

// // Method to load all songs from the database
// public static void loadSongs() {
// String sql = "SELECT * FROM AllSongs";
// try (PreparedStatement st = con.prepareStatement(sql);
// ResultSet rs = st.executeQuery()) {

// songPaths.clear(); // Clear the list before adding songs
// songTitles.clear(); // Clear the list of song titles

// while (rs.next()) {
// songTitles.add(rs.getString("Title"));
// songPaths.add(rs.getString("Path"));
// }

// } catch (Exception e) {
// e.printStackTrace();
// }
// }

// // Method to play a specific song by name
// public static boolean playSong(String songName) {
// for (int i = 0; i < songTitles.size(); i++) {
// if (songTitles.get(i).equalsIgnoreCase(songName)) {
// currentSongIndex = i; // Set the current song index
// DemoMusicAudio.playSound(songPaths.get(i)); // Play the song
// isSongPlaying = true; // Mark that a song is playing
// return true;
// }
// }
// System.out.println("Song not found in the playlist.");
// return false;
// }

// // Method to play the next song
// public static void nextSong() {
// if (currentSongIndex < songPaths.size() - 1) {
// currentSongIndex++; // Move to the next song
// DemoMusicAudio.playSound(songPaths.get(currentSongIndex));
// } else {
// System.out.println("This is the last song in the playlist.");
// }
// }

// // Method to play the previous song
// public static void previousSong() {
// if (currentSongIndex > 0) {
// currentSongIndex--; // Move to the previous song
// DemoMusicAudio.playSound(songPaths.get(currentSongIndex));
// } else {
// System.out.println("This is the first song in the playlist.");
// }
// }

// // Method to display the control menu and switch case logic
// public static void displayMenu() {
// Scanner scanner = new Scanner(System.in);
// boolean exit = false;

// while (!exit) {
// if (isSongPlaying) {
// System.out.println("\n=== Music Player Home ===");
// System.out.println("1. Play a specific song");
// System.out.println("2. Play next song");
// System.out.println("3. Play previous song");
// System.out.println("4. Stop and Exit");
// System.out.print("Enter your choice: ");
// } else {
// System.out.println("\n=== Music Player Menu ===");
// System.out.println("1. Play a specific song");
// System.out.println("4. Exit");
// System.out.print("Enter your choice: ");
// }

// int choice = scanner.nextInt();
// scanner.nextLine(); // Consume the newline

// switch (choice) {
// case 1:
// System.out.print("Enter song name to play: ");
// String songName = scanner.nextLine();
// if (playSong(songName)) {
// System.out.println("Playing: " + songName);
// }
// break;

// case 2:
// if (isSongPlaying) {
// nextSong();
// } else {
// System.out.println("No song is currently playing. Please play a song
// first.");
// }
// break;

// case 3:
// if (isSongPlaying) {
// previousSong();
// } else {
// System.out.println("No song is currently playing. Please play a song
// first.");
// }
// break;

// case 4:
// if (isSongPlaying) {
// isSongPlaying = false; // Stop the current song
// System.out.println("Stopping the music player...");
// }
// exit = true;
// System.out.println("Exiting the music player...");
// break;

// default:
// System.out.println("Invalid choice. Please try again.");
// }
// }

// scanner.close();
// }
// }
