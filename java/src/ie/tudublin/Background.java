package ie.tudublin;

import processing.core.*;

public class Background {
    Visual visual; // Reference to the Visual class
    float gradient = 0; // Declare the gradient variable

    public Background(Visual visual) {
        this.visual = visual;
    }

    public void display() {
        visual.background(gradient);
        gradient += 0.01;
        if (gradient > 255) {
            gradient = 0;
        }
    }
}

