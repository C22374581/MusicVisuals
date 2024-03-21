package example;

import processing.core.PApplet;
import java.util.ArrayList;

public class Thunderstorm {
    PApplet parent; // Reference to the main sketch
    boolean isFlashing = false; // Tracks whether a flash is currently happening
    float flashAlpha = 0; // Alpha value for the flash effect
    ArrayList<Integer> thunderTimes; // Timestamps for "thunder" in the song
    int nextThunderIndex = 0; // Index to track the next "thunder" occurrence

    // Constructor
    Thunderstorm(PApplet parent) {
        this.parent = parent;
        initializeThunderTimes();
    }

    void initializeThunderTimes() {
        thunderTimes = new ArrayList<Integer>();
        // Add all your timestamps here in milliseconds (converted from your seconds list)
        int[] timesInSeconds = {29,33,36,40,44,47,51,54,58,61,70,77,85,92,100,111,161,165,168,171,222,225,229,232,250,254,258,260,265,268,271,275,278};
        for (int time : timesInSeconds) {
            thunderTimes.add(time * 1000); // Convert seconds to milliseconds
        }
    }

    void update(int currentTime) {
        // Trigger a flash when the current time matches the next "thunder" time
        if (!thunderTimes.isEmpty() && currentTime >= thunderTimes.get(nextThunderIndex)) {
            isFlashing = true;
            flashAlpha = 255; // Full alpha for a bright flash
            nextThunderIndex++;
            if (nextThunderIndex >= thunderTimes.size()) {
                nextThunderIndex = 0; // Reset the index if we reach the end
            }
        }

        // Fade out the flash if it's happening
        if (isFlashing) {
            flashAlpha -= 15; // Adjust for faster or slower fade
            if (flashAlpha <= 0) {
                isFlashing = false;
                flashAlpha = 0;
            }
        }
    }

    void display() {
        // If there's a flash, draw a white rectangle over the screen with the current alpha
        if (isFlashing) {
            parent.fill(255, flashAlpha);
            parent.rect(0, 0, parent.width, parent.height);
        }
    }
}
