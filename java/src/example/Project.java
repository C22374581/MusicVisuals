package example;

import java.util.ArrayList;

import ie.tudublin.Visual;
import ie.tudublin.VisualException;



public class Project extends Visual {
    // Terrain dimensions
    int cols, rows;
    int scl = 20; // Scale of each cell in the terrain
    float[][] terrain;
    float terrainOffset = 0;
    float camX = 0;
    float camY = -200;
    float camZ = 500;
    float rotX = PI / 3;
    float zoom = -500;
    boolean moveLeft = false;
    boolean moveRight = false;
    boolean moveUp = false;
    boolean moveDown = false;
    boolean zoomIn = false;
    boolean zoomOut = false;

    public void settings() {
        size(800, 800, P3D);
        println("CWD: " + System.getProperty("user.dir"));
        // fullScreen(P3D, SPAN);
    }

    // Handle key press events
    public void keyPressed() {
        if (key == CODED) {
            if (keyCode == UP) moveUp = true;
            if (keyCode == DOWN) moveDown = true;
            if (keyCode == LEFT) moveLeft = true;
            if (keyCode == RIGHT) moveRight = true;
        } else if (key == '=') {
            zoomIn = true;
        } else if (key == '-') {
            zoomOut = true;
        }
    }

    // Handle key release events
    public void keyReleased() {
        if (key == CODED) {
            if (keyCode == UP) moveUp = false;
            if (keyCode == DOWN) moveDown = false;
            if (keyCode == LEFT) moveLeft = false;
            if (keyCode == RIGHT) moveRight = false;
        } else if (key == '=') {
            zoomIn = false;
        } else if (key == '-') {
            zoomOut = false;
        }
    }
ArrayList<Raindrop> raindrops; // Declare the collection of Raindrop objects
    // Setup the terrain and audio
    public void setup() {
        colorMode(HSB);
        noCursor();

        setFrameSize(256);

        startMinim();
        loadAudio("Kodak.mp3");
        getAudioPlayer().play();
        // startListening(); 
        size(800, 800, P3D);
        raindrops = new ArrayList<Raindrop>(); // Initialize the ArrayList
        for (int i = 0; i < 500; i++) { // Create 500 raindrops as an example
            raindrops.add(new Raindrop(this));
        }
        scl = 20; // Smaller values mean more detail
        cols = (width / scl) * 2; // Extend beyond screen width
        rows = (height / scl) * 2; // Extend beyond screen height
        terrain = new float[cols][rows];
    }

    // Draw the terrain
    public void draw() {
        background(0);
        directionalLight(255, 255, 255, 1, 0, -1); // Adjusted light direction
        calculateAverageAmplitude();
        try {
            calculateFFT();
        } catch(VisualException e) {
            e.printStackTrace();
        }
        calculateFrequencyBands();
        for (Raindrop drop : raindrops) {
            drop.update();
            drop.display();
        }
        float rotationSpeed = (float) 0.01;
        float zoomSpeed = 5;
    
        if (moveUp) rotX -= rotationSpeed;
        if (moveDown) rotX += rotationSpeed;
        if (moveLeft) camX -= zoomSpeed;
        if (moveRight) camX += zoomSpeed;
        if (zoomIn) zoom += zoomSpeed;
        if (zoomOut) zoom -= zoomSpeed;
    
        // Camera setup
        translate(width / 2 + camX, height / 2 + camY, zoom);
        rotateX(rotX);
        translate(-width / 2, -height / 2);
    
        float amplitude = getSmoothedAmplitude(); // Ensure this is not always zero
    
        // Generate terrain
        float yOffset = terrainOffset;
        for (int y = 0; y < rows; y++) {
            float xOffset = 0;
            for (int x = 0; x < cols; x++) {
                terrain[x][y] = map(noise(xOffset, yOffset), 0, 1, -100, 100);
                xOffset += 0.1;
            }
            yOffset += 0.1;
        }
    
        // Draw the terrain
        translate(0, height / 2, -200);
        for (int y = 0; y < rows - 1; y++) {
            beginShape(TRIANGLE_STRIP);
            for (int x = 0; x < cols; x++) {
                float elevation = terrain[x][y];
                float peakThreshold = -50 + 250 * amplitude;
                if (elevation > peakThreshold) {
                    stroke(255, 0, 0); // Red for peaks
                } else {
                    stroke(255); // White for the rest
                }
                vertex(x * scl, y * scl, elevation);
                vertex(x * scl, (y + 1) * scl, terrain[x][y + 1]);
            }
            endShape();
        }
    
        terrainOffset += 0.05; // Move the terrain over time

        

    }    
}

