package example;

import processing.core.PApplet;
import processing.core.PVector;

public class Raindrop {
    PApplet parent; // Reference to the main sketch
    PVector position;
    PVector velocity;
    
    public Raindrop(PApplet p) {
        this.parent = p;
        position = new PVector(parent.random(parent.width), parent.random(-500, 0), parent.random(-500, 500));
        velocity = new PVector(parent.random(-1, 1), parent.random(10, 20), 0);
    }
    
    public void update() {
        position.add(velocity);
        if (position.y > parent.height) {
            position.y = parent.random(-200, 0);
            position.x = parent.random(parent.width);
            position.z = parent.random(-500, 500);
        }
    }
    
    public void display() {
        parent.stroke(200, 200, 255, 150);
        parent.strokeWeight(2);
        parent.line(position.x, position.y, position.z, position.x, position.y + 10, position.z);
    }
}
