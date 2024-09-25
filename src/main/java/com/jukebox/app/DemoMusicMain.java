// package com.jukebox.app;

// import java.io.BufferedReader;
// import java.io.InputStreamReader;

// class DemoMusicMain {
// DemoMusicMain() {
// int ch;
// // print menu:
// boolean ans = true;
// try {
// while (ans) {
// System.out.println("What You Expect From Us To Do?");
// System.out.println("1)Add song to database");
// System.out.println("2)Get the list of all the songs");
// System.out.println("3)Get a particular song detail");
// System.out.println("4)Play a Song");
// BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
// System.out.print("Enter Choice: ");
// // int userInput = MainApp.getIntegerInput(scanner, "Please enter a number:
// ");
// ch = Integer.parseInt(sc.readLine());

// switch (ch) {
// case 1:
// System.out.print("Enter song name: ");
// String songName = sc.readLine();
// System.out.print("Enter Artist name: ");
// String artistName = sc.readLine();
// System.out.print("Enetr song path: ");
// String path = sc.readLine();

// ans = Song.AddSongToDB(songName, artistName, path);
// break;

// case 2:
// // List of all the songs in database:
// ans = Song.getAllSongs();
// break;

// case 3:
// // particular song detail:
// System.out.print("Song Title: ");
// String song_name = sc.readLine();
// String songDetails = Song.particularSong(song_name);
// System.out.println(" song: " + songDetails);
// break;

// case 4:
// // Play a song:
// System.out.print("Enter Song Title: ");
// String name = sc.readLine();
// PlaySong.playSong(name);
// break;

// default:
// System.out.println("Invalid choice!");
// }

// System.out.println("Do you want to continue?\n1.Yes\n2.No");
// int option = Integer.parseInt(sc.readLine());
// if (option != 1) {
// ans = false;
// break;
// }
// }
// } catch (Exception e) {
// e.printStackTrace();
// }
// }
// }
