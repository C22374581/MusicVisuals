package example;

import processing.core.PApplet;

public class Project2 extends PApplet {

    Particle[] swarm;
    int size_swarm = 2000;

    public void settings() {
        size(800, 950, P3D); // Call size here, but not colorMode
    }

    public void setup(){
        colorMode(HSB, 360, 100, 100); // colorMode can now be called safely
        swarm = new Particle[size_swarm];
        for (int i = 0; i < size_swarm; i++) {
            swarm[i] = new Particle(i);
        }
        noStroke();
        background(0);
    }

    public void draw() {
        background(0);
        for (Particle particle : swarm) {
            particle.update();
            particle.display();
        }        
    }

    class Particle {
        float x,y;
        int c; // Use Processing's color type, not java.awt.Color
        float angle;

        Particle(int i){
            x = random(width);
            y = random(height);
            c = color(random(360), 100, 100); // Using HSB color mode
            angle =  i * (2*PI)/size_swarm;
            y = (float) (height*0.5 + height*0.4*sin(angle));
            x = (float) (width*0.5 + (y-(0.1*height))*0.3 *cos(61 * angle));
            
        }

        void update(){
            // Update properties if necessary
        }
        
        void display(){
            fill(c);
            circle(x,y,4);
        }
    }

    public static void main(String[] args) {
        PApplet.main("Project2");
    }
}