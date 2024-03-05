package example;

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



    public void settings()
    {
        size(800, 800, P3D);
        println("CWD: " + System.getProperty("user.dir"));
        //fullScreen(P3D, SPAN);
    }

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

    public void setup()
    {
        colorMode(HSB);
        noCursor();
        
        setFrameSize(256);

        startMinim();
        loadAudio("Kodak.mp3");
        getAudioPlayer().play();
        //startListening(); 
        size(800, 800, P3D);
        scl = 20; // Smaller values mean more detail
        cols = (width / scl) * 2; // Extend beyond screen width
        rows = (height / scl) * 2; // Extend beyond screen height
        terrain = new float[cols][rows];
    


        
    }

    float radius = 200;

    float smoothedBoxSize = 0;

    float rot = 0;

    public void draw() {
        background(0);
        lights(); // Add some basic lighting
        calculateAverageAmplitude();
        try {
            calculateFFT();
        } catch(VisualException e) {
            e.printStackTrace();
        }
        calculateFrequencyBands();
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
    translate(-width / 2, -height * 0.6f);

    
        float amplitude = getSmoothedAmplitude(); // Get the current amplitude
    
        // Adjust camera to ensure the terrain is centered and fully visible
        translate(width / 2, height / 2, -500); // Adjust Z based on terrain size
        rotateX(PI / 3);
        // Centering the terrain: Adjust based on actual terrain dimensions
        translate(-cols * scl / 2, -rows * scl / 2 + 100, 0); // Adjust Y offset to center, may need tweaking
    
        // Dynamic offset for terrain to create slower movement
        terrainOffset += 0.025; // Slower movement
    
        // Generate terrain elevations based on noise, scaled by amplitude
        float yOffset = terrainOffset;
        for (int y = 0; y < rows; y++) {
            float xOffset = 0;
            for (int x = 0; x < cols; x++) {
                terrain[x][y] = map(noise(xOffset, yOffset), 0, 1, -200 * amplitude, 200 * amplitude);
                xOffset += 0.1; // Adjust for desired detail level
            }
            yOffset += 0.1;
        }
    
        // Draw the terrain with adjusted stroke for visibility
        noFill();
        strokeWeight(1); // Optional: adjust stroke weight for better visibility
        for (int y = 0; y < rows - 1; y++) {
            beginShape(TRIANGLE_STRIP);
            for (int x = 0; x < cols; x++) {
                stroke(255 * amplitude, 255, 255); // Color changes with amplitude
                vertex(x * scl, y * scl, terrain[x][y]);
                vertex(x * scl, (y + 1) * scl, terrain[x][y + 1]);
            }
            endShape();
        }
    }
    
}
    