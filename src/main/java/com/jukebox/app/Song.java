package com.jukebox.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.Statement;

class Song {
    String title;
    String artist;
    String path;
    int duration;
    static Connection con = ConnectionClassForDM.getConnection();

    // Constructor to initialize the Song object
    public Song(String title, String artist, int duration, String path) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.path = path;
    }

    public String gettitle() {
        return title;
    }

    public String getartist() {
        return artist;
    }

    public int getduration() {
        return duration;
    }

    public String getpath() {
        return path;
    }

    // Add song to database:
    public static boolean AddSongToDB(String songName, String artistName, String path) {
        String query = "INSERT INTO AllSongs(Title, Artist, Path) VALUES(?, ?, ?)";
        try (PreparedStatement stat = con.prepareStatement(query)) {
            stat.setString(1, songName);
            stat.setString(2, artistName);
            stat.setString(3, path);

            // Execute the query
            if (stat.executeUpdate() == 1) {
                System.out.println("Song Added Successfully!");
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error adding song to database:");
            e.printStackTrace();
        }
        return false;
    }

    // Get all song names from the database:
    public static boolean getAllSongs() {
        String query = "SELECT * FROM AllSongs";
        try (PreparedStatement stat = con.prepareStatement(query);
                ResultSet rs = stat.executeQuery()) {

            System.out.println("Songs in the database:");
            while (rs.next()) {
                String title = rs.getString("Title");
                System.out.println(title);
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error fetching songs from database:");
            e.printStackTrace();
        }
        return false;
    }

    // Get the details of a particular song by title:
    public static String particularSong(String title) {
        String query = "SELECT * FROM AllSongs WHERE Title = ?";
        try (PreparedStatement stat = con.prepareStatement(query)) {
            stat.setString(1, title);
            ResultSet rs = stat.executeQuery();

            if (rs.next()) {
                String songName = rs.getString("Title");
                String artist = rs.getString("Artist");
                String path = rs.getString("Path");
                return "Song_name: " + songName + " | Artist_name: " + artist + " | File_Path: " + path;
            } else {
                System.out.println("Song not found.");
            }
        } catch (Exception e) {
            System.err.println("Error fetching song details:");
            e.printStackTrace();
        }
        return null;
    }

    // Override toString method:
    @Override
    public String toString() {
        return "Song_Name: " + title + " | Artist_Name: " + artist + " | File_Path: " + path;
    }
}
