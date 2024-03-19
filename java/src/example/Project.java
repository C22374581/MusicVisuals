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
    float camX = 500;
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
    boolean isPaused = false; // Track whether the program is paused
    float modStrength = 20; // Strength of the modulation effect


    // Limits for camera movement
final float MAX_CAM_X = 800;
final float MIN_CAM_X = 200;
final float MAX_CAM_Y = 100;
final float MIN_CAM_Y = -500;
final float MAX_ZOOM = 100;
final float MIN_ZOOM = -1000;

// Limits for rotation
final float MAX_ROT_X = PI / 2;
final float MIN_ROT_X = 0;





    public void settings() {
        size(800, 800, P3D);
        println("CWD: " + System.getProperty("user.dir"));
        // fullScreen(P3D, SPAN);
    }

    // Handle key press events
  
    public void keyPressed() {
        if (key == ' ') { // Check if the spacebar is pressed
            isPaused = !isPaused; // Toggle pause state
        } else if (key == CODED) {
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

    public void mouseMoved() {
        // Define the area of the screen where the terrain is drawn
        float terrainWidthRatio = 0.8f; // Replace with actual ratio
        float terrainHeightRatio = 0.8f; // Replace with actual ratio
    
        int terrainStartX = (int) ((1 - terrainWidthRatio) / 2 * width);
        int terrainEndX = (int) ((1 + terrainWidthRatio) / 2 * width);
        int terrainStartY = (int) ((1 - terrainHeightRatio) / 2 * height);
        int terrainEndY = (int) ((1 + terrainHeightRatio) / 2 * height);
    
        // Check if the mouse is within the terrain area
        if (mouseX >= terrainStartX && mouseX <= terrainEndX && mouseY >= terrainStartY && mouseY <= terrainEndY) {
            camX = map(mouseX, terrainStartX, terrainEndX, MIN_CAM_X, MAX_CAM_X);
            camY = map(mouseY, terrainStartY, terrainEndY, MIN_CAM_Y, MAX_CAM_Y);
        }
    }


    ArrayList<Raindrop> raindrops; // Declare the collection of Raindrop objects
    
        // Method to modify the terrain based on mouse position
    void modifyTerrain(int mouseX, int mouseY, float strength) {
    // Calculate the terrain grid position corresponding to the mouse position
    int gridX = (int)((mouseX - width / 2 - camX + (cols * scl) / 2) / scl);
    int gridY = (int)((mouseY - height / 2) / scl);
    
    // Check if the calculated position is within the bounds of the terrain array
    if (gridX >= 0 && gridX < cols && gridY >= 0 && gridY < rows) {
        // Modify the terrain elevation at the calculated position
        // Use a simple modification for demonstration: raise/lower by a fixed amount
        terrain[gridX][gridY] += strength;
        // Prevent the terrain from going below a certain threshold, if necessary
        // terrain[gridX][gridY] = max(terrain[gridX][gridY], minHeight);
    }
}
    // Setup the terrain and audio
    public void setup() {
        colorMode(HSB);
        

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
        cols = (width / scl) * 3; // Extend beyond screen width
        rows = (height / scl) * 3; // Extend beyond screen height
        terrain = new float[cols][rows];
    }

    // Draw the terrain
    public void draw() {
        if (isPaused) {
            return; // Skip the rest of the draw() function
        }
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
        float rotationSpeed = 0.01f;
        float zoomSpeed = 5;
        
        if (moveUp && rotX > MIN_ROT_X) rotX -= rotationSpeed;
        if (moveDown && rotX < MAX_ROT_X) rotX += rotationSpeed;
        if (moveLeft && camX > MIN_CAM_X) camX -= zoomSpeed;
        if (moveRight && camX < MAX_CAM_X) camX += zoomSpeed;
        if (zoomIn && zoom < MAX_ZOOM) zoom += zoomSpeed;
        if (zoomOut && zoom > MIN_ZOOM) zoom -= zoomSpeed;
        
        // Camera setup
        translate(width / 2 + camX - (cols * scl) / 2, height / 2 + camY, zoom);
        rotateX(rotX);
        translate(-width / 2, -height / 2);
    
        float amplitude = getSmoothedAmplitude(); // Ensure this is not always zero
    
        // Generate terrain
        float yOffset = terrainOffset;
        for (int y = 0; y < rows - 1; y++) {
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
    
    public void mousePressed() {
        modifyTerrain(mouseX, mouseY, modStrength);
    }

    public void mouseDragged() {
        modifyTerrain(mouseX, mouseY, modStrength);
    }

}