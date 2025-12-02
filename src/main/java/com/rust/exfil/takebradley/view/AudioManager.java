package com.rust.exfil.takebradley.view;

import javafx.scene.media.AudioClip;

public class AudioManager {
    private AudioClip hitSound;
    
    
    public void loadSounds() {
        hitSound = loadSound("/sounds/hit.wav");
    }
    
    // load a sound clip from path
    private AudioClip loadSound(String path) {
        try {
            String soundUrl = getClass().getResource(path).toString();
            AudioClip clip = new AudioClip(soundUrl);
            return clip;
        } catch (Exception e) {
            System.err.println("Failed to load sound: " + path + " - " + e.getMessage());
            return null;
        }
    }
    
    // play hit sound when entity is hit
    public void playHitSound() {
        if (hitSound != null) {
            hitSound.play();
        }
    }
}
