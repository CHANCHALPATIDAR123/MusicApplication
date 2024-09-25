package com.jukebox.app;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int userID;
    private String username;
    private String password;
    private List<Playlists> playlists;

    // Constructor to initialize user
    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.playlists = new ArrayList<>();
        getPlaylists(); // Load playlists from the database upon user creation
    }

    // Getters
    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Playlists> getPlaylists() {
        return playlists;
    }

    // Method to create a playlist and save it to the database

}
