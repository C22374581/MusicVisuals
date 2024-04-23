package example;

import processing.core.PApplet;
import processing.core.PVector;

class Particle {
    PVector position;
    PVector velocity;
    float lifespan;

    Particle(PApplet parent) {
        position = new PVector(parent.random(parent.width), parent.random(parent.height));
        velocity = PVector.random2D();
        velocity.mult(parent.random(0.5f, 2.0f)); // Randomize speed
        lifespan = 255; // Start fully opaque
    }

    void update() {
        position.add(velocity);
        lifespan -= 2; // Decrease lifespan
    }

    void display(PApplet parent) {
        parent.stroke(255, lifespan); // Use lifespan for alpha
        parent.strokeWeight(2);
        parent.point(position.x, position.y);
    }

    boolean isDead() {
        return lifespan < 0;
    }
}

