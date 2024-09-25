package com.jukebox.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Album {
    Connection con = ConnectionClassForDM.getConnection(); // Establish database connection
    Scanner sc = new Scanner(System.in);

    // Constructor
    public Album() {
    }

    // Method to view all albums
    public void viewAllAlbum() {
        try {
            System.out.println("\nAvailable Albums:");
            String sql = "SELECT AlbumName FROM Album";
            PreparedStatement stat = con.prepareStatement(sql);
            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString("AlbumName")); // Display album names
            }
        } catch (SQLException e) {
            System.out.println("Error: Unable to fetch albums.");
            e.printStackTrace();
        }
    }

    // Method to view all songs from a particular album
    public void viewAlbumSongs() {
        System.out.print("Enter Album name: ");
        String albumName = sc.nextLine(); // Get album name from user

        try {
            String qur = "SELECT Album.AlbumName, allsongs.Title, allsongs.Artist "
                    + "FROM Album "
                    + "INNER JOIN AlbumSongs ON Album.AlbumID = AlbumSongs.AlbumID "
                    + "INNER JOIN allsongs ON AlbumSongs.SongID = allsongs.SongID "
                    + "WHERE Album.AlbumName = ?";
            PreparedStatement statement = con.prepareStatement(qur);
            statement.setString(1, albumName); // Set album name in query
            ResultSet rs = statement.executeQuery();

            if (!rs.isBeforeFirst()) { // Check if the result set is empty
                System.out.println("No songs found for the album: " + albumName);
            } else {
                while (rs.next()) {
                    String title = rs.getString("Title");
                    String artist = rs.getString("Artist");
                    System.out.println("Title: " + title + " | Artist: " + artist);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: Unable to fetch songs from album.");
            e.printStackTrace();
        }
    }

    // Method to play a song from the selected album
    public void playAlbumSong() {
        System.out.print("Enter Album name: ");
        String albumName = sc.nextLine(); // Get album name from user

        try {
            String sql = "SELECT allsongs.Title, allsongs.Artist, allsongs.Path "
                    + "FROM Album "
                    + "INNER JOIN AlbumSongs ON Album.AlbumID = AlbumSongs.AlbumID "
                    + "INNER JOIN allsongs ON AlbumSongs.SongID = allsongs.SongID "
                    + "WHERE Album.AlbumName = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, albumName); // Set album name in query
            ResultSet rs = statement.executeQuery();

            if (!rs.isBeforeFirst()) { // Check if the result set is empty
                System.out.println("No songs found for the album: " + albumName);
            } else {
                while (rs.next()) {
                    String title = rs.getString("Title");
                    String artist = rs.getString("Artist");
                    String path = rs.getString("Path");

                    System.out.println("Playing: " + title + " | Artist: " + artist);
                    DemoMusicAudio.playSound(path); // Play song using DemoMusicAudio
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: Unable to fetch and play song from album.");
            e.printStackTrace();
        }
    }
}
