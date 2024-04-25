package example;

import java.util.ArrayList;
import ie.tudublin.Visual;
import ie.tudublin.VisualException;


public class Project extends Visual {
    float[] coswave;
    int shapeIndex = 0;
    int[] colors = { // Array of colors to interpolate between
      color(255, 0, 0), // Red
      color(0, 255, 0), // Green
      color(0, 0, 255)  // Blue
    };
    int cols, rows;
    int scl = 20; // Scale for each cell
    float[][] terrain; // Terrain height map
    float terrainOffset = 0;
    float camX = 0, camY = -750, camZ = 500;
    float rotX = PI / 3;
    float zoom = -1260;
    boolean moveLeft = false, moveRight = false, moveUp = false, moveDown = false, zoomIn = false, zoomOut = false;
    boolean isPaused = false;
    boolean showInstructions = true;

    final float MAX_CAM_X = 800, MIN_CAM_X = 200, MAX_CAM_Y = 100, MIN_CAM_Y = -500;
    final float MAX_ZOOM = 100, MIN_ZOOM = -1000;
    final float MAX_ROT_X = PI / 2, MIN_ROT_X = 0;
    
    ArrayList<Raindrop> raindrops;
    ArrayList<Snowflake> snowflakes;
    ArrayList<Particle> particles;
    Thunderstorm thunderstorm;
    String currentWeather = "bloodmoon"; // Default weather condition
    
    



    public void settings() {
        fullScreen(P3D);
        println("CWD: " + System.getProperty("user.dir"));
        

        // Initialize weather systems
        raindrops = new ArrayList<Raindrop>();
        snowflakes = new ArrayList<Snowflake>();
        thunderstorm = new Thunderstorm(this); //  use Processing methods inside the Thunderstorm class

        for (int i = 0; i < 500; i++) {
            raindrops.add(new Raindrop(this));
            snowflakes.add(new Snowflake(this));
        }
    }
    

    public void keyPressed() {
        showInstructions = false;
        if (key == CODED) {
            switch (keyCode) {
                case UP:
                    moveUp = true;
                    break;
                case DOWN:
                    moveDown = true;
                    break;
                case LEFT:
                    moveLeft = true;
                    break;
                case RIGHT:
                    moveRight = true;
                    break;
            }
        } else {
            switch (key) {
                case ' ':
                    isPaused = !isPaused;
                    if (isPaused) {
                        noLoop();
                    } else {
                        loop();
                    }
                    break;
                case '=':
                    zoomIn = true;
                    break;
                case '-':
                    zoomOut = true;
                    break;
        
                case 'r': // Rain
                    currentWeather = "rain";
                    loadRainSong();
                    break;
                case 'f': // Fog
                    currentWeather = "fog";
                    loadFogSong(); // Method to load and play fog.mp3
                    break;
                case 's': // Snow
                    currentWeather = "snow";
                    loadSnowSong();
                    break;
                case 'C':
                case 'c': // Reset to default state, which is the blood moon
                    currentWeather = "bloodMoon";
                    break;
            }
        }
    }
    
    
    
    public void keyReleased() {
        if (key == CODED) {
            switch (keyCode) {
                case UP:
                    moveUp = false;
                    break;
                case DOWN:
                    moveDown = false;
                    break;
                case LEFT:
                    moveLeft = false;
                    break;
                case RIGHT:
                    moveRight = false;
                    break;
            }
        } else {
            // Since zoomIn and zoomOut are toggled on key press, no need to toggle them off on key release for '=' and '-'
        }
    }
    
    public void pause() {
        if (isPaused) noLoop();
        else loop();
    }

    
    public void setup() {
        coswave = new float[width];
        for (int i = 0; i < width; i++) {
          float amount = map(i, 0, width, 0, PI);
          coswave[i] = abs(cos(amount));
        }
        colorMode(HSB);
        setFrameSize(256);
        startMinim();
        loadAudio("Cudi.mp3");
        getAudioPlayer().play();
        raindrops = new ArrayList<Raindrop>();
        for (int i = 0; i < 500; i++) raindrops.add(new Raindrop(this));
        scl = 20;
        cols = (int) ((width / scl) * 1.5);
        rows = (int) ((height / scl) * 1.5);
        terrain = new float[cols][rows];
        particles = new ArrayList<Particle>();
        for (int i = 0; i < 100; i++) { // Start with 100 particles
            particles.add(new Particle(this));
        }

        // Set text properties for the instruction message
    textSize(24); // Set the text size
    fill(255, 255, 255); // Set the text color to white for visibility
    textAlign(CENTER, CENTER); // Center the text horizontally and vertically

    }

// Updated to accept amplitude as an argument
void generateTerrain(float amplitude) {
    float yOffset = terrainOffset;
    for (int y = 0; y < rows; y++) {
        float xOffset = 0;
        for (int x = 0; x < cols; x++) {
            // Modulate the height based on noise and amplitude
            float elevation = map(noise(xOffset, yOffset), 0, 1, -100 * amplitude, 100 * amplitude);
            terrain[x][y] = elevation;
            xOffset += 0.1; // Fine-grained noise for more detailed terrain
        }
        yOffset += 0.1;
    }
}


    int getSongPosition() {
        return getAudioPlayer().position();
    }

    public void draw() {

    background(0); // Otherwise, clear the screen with a black background
        calculateAverageAmplitude(); // Calculate the average amplitude
        try {
            calculateFFT();
        } catch (VisualException e) {
            e.printStackTrace();
        }
        calculateFrequencyBands(); // Calculate the frequency bands
        float amplitude = getSmoothedAmplitude() * 5; // Amplify the amplitude    
        // Weather system rendering based on currentWeather state
        switch (currentWeather) {
            case "rain":
                for (Raindrop drop : raindrops) {
                    drop.update();
                    drop.display();
                }

                amplitude = constrain(amplitude, 0, 1); // Constrain to [0, 1] range
                translate(width / 2 + camX - (cols * scl) / 2, height / 2 + camY, zoom);
                rotateX(rotX);
                terrainOffset += 0.05 + amplitude * 0.5;
                generateTerrain(amplitude);
                drawTerrain(amplitude);
                terrainOffset += 0.001;                
                break;
            case "fog":
            // Draw elements that should appear behind the fog:
            drawTerrain3(amplitude);
            terrainOffset += 0.001;  
            break;   
                
            case "snow":
                for (Snowflake flake : snowflakes) {
                    flake.update();
                    flake.display();                  
                }
                drawTerrain2(amplitude);
                terrainOffset += 0.001;  
                break;
        }
    
        // Camera and view adjustments based on key inputs
        float movementSpeed = 2; // Adjust this value as needed for movement sensitivity
        float zoomSpeed = 20; // Adjust for zoom sensitivity
        if (moveUp) camY += movementSpeed;
        if (moveDown) camY -= movementSpeed;
        if (moveLeft) camX += movementSpeed;
        if (moveRight) camX -= movementSpeed;
        if (zoomIn) zoom += zoomSpeed;
        if (zoomOut) zoom -= zoomSpeed;
    
    


        // Update and display particles
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.update();
            p.display(this);
            if (p.isDead()) {
                particles.remove(i);
                particles.add(new Particle(this)); // Replace dead particle with a new one
            }
}

    if (showInstructions) {
    textSize(24);
    fill(255, 255, 255);
    textAlign(CENTER, CENTER);
    text("Press 'R' for Rain, 'S' for Boxes, 'F' for Stars", width / 2, height - 50);
    }


    }
    
    void drawTerrain(float amplitude) {
        // Visualize the terrain
        for (int y = 0; y < rows - 1; y++) {
            beginShape(TRIANGLE_STRIP);
            for (int x = 0; x < cols; x++) {
                float baseElevation = terrain[x][y];
                float elevation = baseElevation + amplitude * 200; // Modulate elevation with amplitude
                stroke(255 * amplitude, 255, 255 - 200 * amplitude);
                fill(0); // You can remove this if you don't want the terrain filled
                vertex(x * scl, y * scl, elevation);
                vertex(x * scl, (y + 1) * scl, terrain[x][y + 1]);
            }
            endShape();
        }
    }



// Declare a global variable to keep track of the rotation
float accumulatedRotation = 0;

void drawTerrain2(float amplitude) {
    background(0);
    translate(width / 2, height / 2, -500); // Center the scene, adjust z for better view

    // Modulate light color and intensity based on amplitude
    float lightIntensity = 100 + amplitude * 155;
    pointLight(lightIntensity, lightIntensity * 0.66f, 0, 200, -150, 0); // Orange light, more intense with amplitude
    directionalLight(0, 102 + amplitude * 153, 255, -1, 0, 0); // Blue light, brighter with amplitude

    // Accumulate rotation for dynamic movement
    accumulatedRotation += amplitude * PI / 10;

    // Configure number of boxes and grid
    int numBoxes = 30; // Total number of boxes
    int gridWidth = 5; // Number of boxes along the width
    int spacing = 300; // Space between each box

    // Draw multiple boxes with vivid color changes
    for (int i = 0; i < numBoxes; i++) {
        pushMatrix();

        // Calculate grid positions
        float x = (i % gridWidth) * spacing - (gridWidth * spacing / 2);
        float y = ((i / gridWidth) % gridWidth) * spacing - (gridWidth * spacing / 2);
        float z = (i % 2) * spacing - spacing / 2;

        translate(x, y, z);

        // Apply the accumulated rotation
        rotateY(accumulatedRotation);
        rotateX(accumulatedRotation);

        // Dynamic size and more random color alteration with amplitude
        float boxSize = 100 + amplitude * 100; // More dramatic size changes

        // Randomly change colors for a "crazy" effect
        int r = (int)(random(255)); // Random red component
        int g = (int)(random(255)); // Random green component
        int b = (int)(random(255)); // Random blue component
        fill(r, g, b); // Adjust color dynamically with completely random values

        box(boxSize);

        popMatrix();
    }    
}


int numStars = 50; // Number of stars
float startRadius = 300; // Initial radius of the circle

void drawTerrain3(float amplitude) {
  clear();
  background(0);

  // Calculate time-based rotation
  float minTimeScale = (float) 0.001; // Minimum time scale
  float maxTimeScale = (float) 1.0; // Maximum time scale
  float timeScale = map(amplitude, 0, 1, minTimeScale, maxTimeScale); // Adjust the time scale based on amplitude
  float time = (float) (millis() / 1000.0) * timeScale; // Convert milliseconds to seconds and scale by timeScale

  // Draw shooting star effects in the background
  drawShootingStars();

  // Draw one big star in the middle
  float middleSize = 50 + amplitude * 100; // Scale size based on amplitude
  float middleX = width / 2;
  float middleY = height / 2;
  float middleColor = amplitude * 255; // Change color based on amplitude

  strokeWeight(2);
  stroke(middleColor);
  fill(middleColor);
  beginShape();
  for (int i = 0; i < 10; i++) {
    float angle = TWO_PI / 10 * i + time; // Add time-based rotation
    float xOffset = cos(angle) * middleSize;
    float yOffset = sin(angle) * middleSize;
    if (i % 2 == 0) {
      xOffset *= 0.5; // Make every other point closer to the center
      yOffset *= 0.5; // Make every other point closer to the center
    }
    vertex(middleX + xOffset, middleY + yOffset);
  }
  endShape(CLOSE);

  // Iterate over each star position
  for (int i = 0; i < numStars; i++) {
    float angle = TWO_PI * i / numStars + time; // Calculate angle for this star
    float radius = startRadius; // Distance from the center big star

    // Scale size based on amplitude
    float size = 10 + amplitude * 40;

    // Generate random RGB values for the star's color
    float r = random(256); // Random red component
    float g = random(256); // Random green component
    float b = random(256); // Random blue component

    // Calculate the position of the star in a circular orbit
    float x = middleX + cos(angle) * radius;
    float y = middleY + sin(angle) * radius;

    // Set stroke and fill colors using the random RGB values
    strokeWeight(2);
    stroke(r, g, b);
    fill(r, g, b);

    beginShape();
    for (int j = 0; j < 10; j++) {
      float angleOffset = TWO_PI / 10 * j;
      float xOffset = cos(angleOffset) * size;
      float yOffset = sin(angleOffset) * size;
      if (j % 2 == 0) {
        xOffset *= 0.5; // Make every other point closer to the center
        yOffset *= 0.5; // Make every other point closer to the center
      }
      vertex(x + xOffset, y + yOffset);
    }
    endShape(CLOSE);
  }
}

// Function to draw shooting star effects in the background
void drawShootingStars() {
  strokeWeight(1);
  stroke(255, 255, 255, 200); // Semi-transparent white lines
  for (int i = 0; i < 10; i++) { // Draw multiple shooting stars
    float startX = random(width); // Random starting x-coordinate
    float startY = random(height); // Random starting y-coordinate
    float endX = startX - random(100, 200); // Random ending x-coordinate
    float endY = startY + random(-20, 20); // Random ending y-coordinate with slight variation
    line(startX, startY, endX, endY); // Draw the shooting star
  }
}
    
    void loadSnowSong() {
        // Ensure there's an audio player available
        if (getAudioPlayer() != null) {
            getAudioPlayer().close(); // Close the current audio player to free resources
        }
        loadAudio("snow.mp3"); // Load the "thunderstruck.mp3" file
        getAudioPlayer().play(); // Play the new song
    }



    void loadRainSong() {
        // Ensure there's an audio player available
        if (getAudioPlayer() != null) {
            getAudioPlayer().close(); // Close the current audio player to free resources
        }
        loadAudio("rain.mp3"); // Load the "rain.mp3" file
        getAudioPlayer().play(); // Play the new song
    }

    void loadFogSong() {
        if (getAudioPlayer() != null) {
            getAudioPlayer().close(); // Close the current audio player to free resources
        }
        loadAudio("californication.mp3"); // Load the "fog.mp3" file
        getAudioPlayer().play(); // Play the fog song
    }
    

    void drawParticles() {
        // Update and display particles
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.update();
            p.display(this);
            if (p.isDead()) {
                particles.remove(i);
                particles.add(new Particle(this)); // Replace dead particle with a new one
            }
        }
    }
    
    
    
}
