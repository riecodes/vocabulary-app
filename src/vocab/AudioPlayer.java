package vocab;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * A utility class for playing audio files in WAV format
 */
public class AudioPlayer {
    
    // Static reference to the currently playing clip
    private static Clip currentClip = null;
    
    /**
     * Checks if audio is currently playing
     * 
     * @return true if audio is playing, false otherwise
     */
    public static boolean isPlaying() {
        return currentClip != null && currentClip.isRunning();
    }
    
    /**
     * Stops any currently playing audio
     */
    public static void stopAudio() {
        if (currentClip != null) {
            currentClip.stop();
            currentClip.close();
            currentClip = null;
        }
    }
    
    /**
     * Plays a WAV audio file, stopping any currently playing audio first
     * 
     * @param audioFile The WAV file to play
     * @return true if playback started successfully, false otherwise
     */
    public static boolean playAudio(File audioFile) {
        // Stop any currently playing audio
        stopAudio();
        
        try {
            // Using try-with-resources to ensure streams are closed
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            
            clip.open(audioStream);
            
            // Store reference to current clip
            currentClip = clip;
            
            // Add a listener to know when playback is complete
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    // Close clip when playback is done
                    clip.close();
                    // Clear reference if this is still the current clip
                    if (currentClip == clip) {
                        currentClip = null;
                    }
                }
            });
            
            // Start playback
            clip.start();
            
            return true;
            
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            System.out.println("Error playing audio: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
} 