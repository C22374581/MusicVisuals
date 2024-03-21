package example;

import java.util.ArrayList;
import ie.tudublin.Visual;
import ie.tudublin.VisualException;

public class Project extends Visual {
    int cols, rows;
    int scl = 20; // Scale for each cell
    float[][] terrain; // Terrain height map
    float terrainOffset = 0;
    float camX = 500, camY = -200, camZ = 500;
    float rotX = PI / 3;
    float zoom = -500;
    boolean moveLeft = false, moveRight = false, moveUp = false, moveDown = false, zoomIn = false, zoomOut = false;
    boolean isPaused = false;
    float modStrength = 20; // Earthquake modulation strength
    boolean regenerateTerrain = true;
    int earthquakeEffectDuration = 0;
    final int earthquakePauseDuration = 30;
    boolean isEarthquakeActive = false; // Flag for earthquake activity

    final float MAX_CAM_X = 800, MIN_CAM_X = 200, MAX_CAM_Y = 100, MIN_CAM_Y = -500;
    final float MAX_ZOOM = 100, MIN_ZOOM = -1000;
    final float MAX_ROT_X = PI / 2, MIN_ROT_X = 0;
    
    ArrayList<Raindrop> raindrops;
    ArrayList<Snowflake> snowflakes;
    Thunderstorm thunderstorm;
    String currentWeather = "bloodmoon"; // Default weather condition


    public void settings() {
        size(800, 800, P3D);
        println("CWD: " + System.getProperty("user.dir"));
        // fullScreen(P3D, SPAN);

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
                case 'E':
                case 'e':
                    //System.out.println("E key pressed. mouseX: " + mouseX + ", mouseY: " + mouseY + ", modStrength: " + modStrength);
                    earthquake(mouseX, mouseY, modStrength);
                    break;
                case 'r': // Rain
                    currentWeather = "rain";
                    break;
                case 'f': // Fog
                    currentWeather = "fog";
                    break;
                case 's': // Snow
                    currentWeather = "snow";
                    break;
                    case 't': // Thunderstorm
                    currentWeather = "thunderstorm";
                    loadThunderstruckSong(); // Load the thunderstruck song
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
        colorMode(HSB);
        setFrameSize(256);
        startMinim();
        loadAudio("Kodak.mp3");
        getAudioPlayer().play();
        raindrops = new ArrayList<Raindrop>();
        for (int i = 0; i < 500; i++) raindrops.add(new Raindrop(this));
        scl = 20;
        cols = (width / scl) * 3;
        rows = (height / scl) * 3;
        terrain = new float[cols][rows];
    }

    public void generateTerrain() {
        if (regenerateTerrain) {
            float yOffset = terrainOffset;
            for (int y = 0; y < rows - 1; y++) {
                float xOffset = 0;
                for (int x = 0; x < cols; x++) {
                    terrain[x][y] = map(noise(xOffset, yOffset), 0, 1, -100, 100);
                    xOffset += 0.1;
                }
                yOffset += 0.1;
            }
        }
    }

    int getSongPosition() {
        return getAudioPlayer().position();
    }

    public void draw() {
        if (isPaused) return;
        directionalLight(255, 255, 255, 1, 0, -1); // Add a directional light from the left
        if (currentWeather.equals("bloodMoon")) {
            background(255, 0, 0); // Set the background to red during a blood moon
        } else {
            background(0); // Otherwise, clear the screen with a black background
        }
        calculateAverageAmplitude(); // Calculate the average amplitude
        try {
            calculateFFT();
        } catch(VisualException e) {
            e.printStackTrace();
        } // Calculate the FFT
        calculateFrequencyBands(); // Calculate the frequency bands
    
        // Weather system rendering based on currentWeather state
        switch (currentWeather) {
            case "rain":
                for (Raindrop drop : raindrops) {
                    drop.update();
                    drop.display();
                }
                break;
            case "fog":
                drawFog(); // Assuming drawFog() is implemented elsewhere
                break;
            case "snow":
                for (Snowflake flake : snowflakes) {
                    flake.update();
                    flake.display();
                }
                break;
            case "thunderstorm":
                int songPosition = getSongPosition(); // Fetch the current song position
                thunderstorm.update(songPosition); // Update thunderstorm with the current song position
                thunderstorm.display();
                for (Raindrop drop : raindrops) {
                    drop.update();
                    drop.display();
                }
                break;

            case "bloodMoon":
                drawBloodMoon();
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
    
        // Apply translations and rotations based on camera position and viewing angle
        translate(width / 2 + camX - (cols * scl) / 2, height / 2 + camY, zoom);
        rotateX(rotX);
    
        // Now draw your terrain and other elements here
        float amplitude = getSmoothedAmplitude(); // This should adjust terrain or other visuals based on the audio amplitude
        generateTerrain(); // Only regenerate terrain if necessary, respecting the earthquake effect
        drawTerrain(amplitude); // Draw the terrain with respect to the audio amplitude

        // Drawing the terrain with respect to the newly updated camera position
        translate(0, height / 2, -200); // Adjusting the terrain drawing position
        drawTerrain(amplitude); // Method call to draw the terrain
        
        // Earthquake effect duration handling
        if (earthquakeEffectDuration > 0) {
            earthquakeEffectDuration--; // Decrement the earthquake effect duration
            if (earthquakeEffectDuration <= 0) {
                regenerateTerrain = true; // Allow terrain regeneration after earthquake effects
            }
        }
    
        terrainOffset += 0.05; // Increment the terrain offset for continuous terrain movement effect

        drawTerrain(getSmoothedAmplitude()); 
    }
    

    void drawTerrain(float amplitude) {
        for (int y = 0; y < rows - 1; y++) {
            beginShape(TRIANGLE_STRIP); 
            for (int x = 0; x < cols; x++) {
                float elevation = terrain[x][y];
                float peakThreshold = -50 + 250 * amplitude;
                stroke(elevation > peakThreshold ? color(255, 0, 0) : color(255));
                vertex(x * scl, y * scl, elevation);
                vertex(x * scl, (y + 1) * scl, terrain[x][y + 1]);
            }
            endShape();
        }
    }

    void drawBloodMoon() {
        // Calculate the moon's position to ensure it's in the top right corner
        float moonDiameter = 150;
        float moonX = width - moonDiameter / 2; // Right corner, minus half the moon's diameter
        float moonY = moonDiameter / 2; // Top part of the screen, plus half the moon's diameter
    
        fill(255, 0, 0); // Red for the blood moon
        ellipse(moonX, moonY, moonDiameter, moonDiameter); // Draw the moon
    
        fill(255); // Reset the fill color to white
    }

    public void earthquake(int mouseX, int mouseY, float strength) {
        isEarthquakeActive = true;
        regenerateTerrain = false;
        earthquakeEffectDuration = earthquakePauseDuration;

        int gridX = (int)((mouseX - width / 2 - camX + (cols * scl) / 2) / scl);
        int gridY = (int)((mouseY - height / 2) / scl);
        for (int x = max(0, gridX - 10); x < min(cols, gridX + 10); x++) {
            for (int y = max(0, gridY - 10); y < min(rows, gridY + 10); y++) {
                terrain[x][y] -= strength;
            }
        }

        isEarthquakeActive = false;
    }

    public void mouseDragged() {
        modifyTerrain(mouseX, mouseY, modStrength);
    }

    public void modifyTerrain(int x, int y, float strength) {
    
     
    }
    void drawFog() {
        // Cover the entire screen with a semi-transparent grey overlay
        // Adjust the alpha value (here set to 150) to make the fog denser or lighter
        fill(185, 185, 185, 185); // Semi-transparent grey
        noStroke();
        rect(0, 0, width, height);
    }
    
    void loadThunderstruckSong() {
        // Ensure there's an audio player available
        if (getAudioPlayer() != null) {
            getAudioPlayer().close(); // Close the current audio player to free resources
        }
        loadAudio("ThunderStruck.mp3"); // Load the "thunderstruck.mp3" file
        getAudioPlayer().play(); // Play the new song
    }
    
    
    
}
