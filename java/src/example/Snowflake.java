package example;

import processing.core.PApplet;
import processing.core.PVector;

public class Snowflake {
    PApplet parent; // Reference to the main sketch
    PVector position;
    PVector velocity;

    // Constructor
    Snowflake(PApplet parent) {
        this.parent = parent;
        reset();
    }

    // Reset or initialize the snowflake
    void reset() {
        position = new PVector(parent.random(parent.width), 0, parent.random(-10, 10));
        velocity = new PVector(parent.random(-1, 1), parent.random(1, 3));
    }

    // Update the snowflake's position
    void update() {
        position.add(velocity);
        // If the snowflake goes off the bottom of the screen, reset it
        if (position.y > parent.height) {
            reset();
        }
    }

    // Display the snowflake
    void display() {
        parent.stroke(255);
        parent.strokeWeight(2);
        parent.point(position.x, position.y);
    }
}
