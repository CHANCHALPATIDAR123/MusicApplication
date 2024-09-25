package com.jukebox.app;

//import java.sql.Connection;
//import java.sql.DriverManager;
import java.io.File;
//import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
//import javax.sound.sampled.LineUnavailableException;
//import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

class DemoMusicAudio {
    public static void playSound(String filePath) {
        try {
            File path = new File(filePath);
            AudioInputStream ais = AudioSystem.getAudioInputStream(path);
            Clip clip = AudioSystem.getClip(); // throws LineUnvailableException;
            clip.open(ais);
            clip.start();

            // music on loop continuously...
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            // to pause the music audio:
            JOptionPane.showMessageDialog(null, "Pause the audio");
            // getMicroSecond shows where we are at the audio in microseconds
            long clipTimePosition = clip.getMicrosecondPosition();
            clip.stop();

            // to resume the audio:
            JOptionPane.showMessageDialog(null, "Resume the audio");
            clip.setMicrosecondPosition(clipTimePosition);
            clip.start();

            // to stop music: show an OK button:
            JOptionPane.showMessageDialog(null, "Press OK to stop audio");
            clip.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
