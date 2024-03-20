package example;

import processing.core.PApplet;

public class Thunderstorm {
    PApplet parent; // Reference to the main sketch
    boolean isFlashing = false; // Tracks whether a flash is currently happening
    float flashAlpha = 0; // Alpha value for the flash effect

    // Constructor
    Thunderstorm(PApplet parent) {
        this.parent = parent;
    }

    void update() {
        // Randomly trigger flashes
        if (parent.random(1) < 0.01) { // Adjust probability to make it rarer or more common
            isFlashing = true;
            flashAlpha = 255;
        }

        // If a flash is happening, decrease its alpha value to fade out
        if (isFlashing) {
            flashAlpha -= 5; // Adjust for faster or slower fade
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
        // update and display raindrops if you integrate rain into the thunderstorm effect

         
    }
}
