package example;
import ddf.minim.*;
import processing.core.PApplet;

public class Project2 extends PApplet {
    float speed; // declare speed at class level
    Minim minim;
    AudioPlayer song;
    Particle[] swarm;
    int size_swarm = 20000;

    public void settings() {
        size(800, 950, P3D); // Call size here, but not colorMode
    }

    public void setup(){
        minim = new Minim(this);
        song = minim.loadFile("rain.mp3");
        song.play();
        colorMode(HSB, 360, 100, 100); // colorMode can now be called safely
        swarm = new Particle[size_swarm];
        for (int i = 0; i < size_swarm; i++) {
            swarm[i] = new Particle(i);
        }
        noStroke();
        background(0);
    }

    public void draw() {
      if (song != null && song.isPlaying()) {
        speed = map(song.mix.level(), 0, 1, 0, 2);
    }
        fill(0,mouseY*(100.0f/height));
        rect(0,0,width,height);
        for (int i = 0; i < size_swarm; i++) {
          swarm[i].update();
        }
        for (int i = 0; i < size_swarm; i++) {
          if((i % (mouseX+1)) == 0) {
          swarm[i].display();
          }
        }                 
    }

    class Particle {
        float x,y;
        int c; // Use Processing's color type, not java.awt.Color
        float angle;

        Particle(int i){
            x = random(width);
            y = random(height);
            c = color(random(0,50), 100, 100); // Using HSB color mode
            angle =  i * (2*PI)/size_swarm;
            y = (float) (height*0.5 + height*0.4*sin(40*angle));
            x = (float) (width*0.5 + (y-(0.1*height))*0.3 *cos(61 * angle));
            
        }

        void update(){
            // Update properties if necessary
            angle += speed * (2*PI)/size_swarm;
            c= color ((hue(c)+1) % 360, 100, 100);
            y = (float) (height*0.5 + height*0.4*sin(20*angle));
            x = (float) (width*0.5 + (y-(0.1*height))*0.3 *cos(61 * angle));
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
