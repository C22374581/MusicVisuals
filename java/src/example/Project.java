package example;

import java.util.ArrayList;
import ie.tudublin.Visual;
import ie.tudublin.VisualException;

public class Project extends Visual {
    int cols, rows;
    int scl = 20; // Scale for each cell
    float[][] terrain; // Terrain height map
    float terrainOffset = 0;
    float camX = 0, camY = -750, camZ = 500;
    float rotX = PI / 3;
    float zoom = -1260;
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
    ArrayList<Particle> particles;
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
                    earthquake(mouseX, mouseY, modStrength);
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
                    break;
                case 't': // Thunderstorm
                    currentWeather = "thunderstorm";
                    loadThunderstruckSong();
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
        cols = (int) ((width / scl) * 1.5);
        rows = (int) ((height / scl) * 1.5);
        terrain = new float[cols][rows];
        particles = new ArrayList<Particle>();
        for (int i = 0; i < 100; i++) { // Start with 100 particles
            particles.add(new Particle(this));
        }

    }

    void drawClover(float x, float y, float size) {
        fill(0, 128, 0); // Green color for clover
        noStroke();
        for (int i = 0; i < 4; i++) {
            pushMatrix();
            translate(x, y);
            rotate(PI / 2 * i);
            // Draw heart shapes for each leaf of the clover
            beginShape();
            vertex(0, -size/4);
            bezierVertex(-size/4, -size/2, -size/2, -size/4, 0, size/4);
            bezierVertex(size/2, -size/4, size/4, -size/2, 0, -size/4);
            endShape(CLOSE);
            popMatrix();
        }
        // Draw stem
        stroke(0, 128, 0); // Green color for the stem
        strokeWeight(2);
        line(x, y + size/4, x, y + size);
    }
    
    
    void drawCelticCross(float x, float y, float size) {
        fill(218, 165, 32); // Golden color for the cross
        noStroke();
        // Vertical part of the cross
        rect(x - size/8, y - size/2, size/4, size);
        // Horizontal part of the cross
        rect(x - size/2, y - size/8, size, size/4);
        // Circle at the center
        ellipse(x, y, size/2, size/2);
    }
    
    
    void drawLandscape() {
        // Gradient sky from light blue to darker
        for (int i = 0; i < height / 2; i++) {
            float inter = map(i, 0, height / 2, 0, 1);
            int c = lerpColor(color(135, 206, 235), color(25, 25, 112), inter);
            stroke(c);
            line(0, i, width, i);
        }
        
        // Green hills
        fill(34, 139, 34); // Use a green color for the hills
        noStroke();
        // Ensure all arguments are float by casting calculations to float where needed
        arc(width / 2, height, (float)(width * 1.2), (float)(height * 0.8), PI, TWO_PI);
        arc(width / 4, height, (float)(width * 0.6), (float)(height * 0.5), PI, TWO_PI);
        arc(width * 3 / 4, height, (float)(width * 0.6), (float)(height * 0.5), PI, TWO_PI);
    }
    
    
    void drawIrishTheme() {
        float musicAmplitude = getSmoothedAmplitude(); // Assume this is implemented
        float size = musicAmplitude * 100; // Example scaling based on music
        
        drawLandscape(); // Background landscape
        drawClover(100, height - 100, size); // Example placement
        drawCelticCross(width - 100, height - 100, size); // Example placement
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
            // Draw elements that should appear behind the fog:
            drawParticles();  // This would be a new method that contains the particle drawing logic.
            drawIrishTheme(); // This would be a new method that contains the Irish theme drawing logic.
            drawTerrain(amplitude);

            // Now draw the fog over everything:
            drawFog();
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
    
        // Calculate smoothed amplitude for terrain modulation
        float amplitude = getSmoothedAmplitude() * 5; // Amplify the amplitude
        amplitude = constrain(amplitude, 0, 1); // Constrain to [0, 1] range
        
        // Apply camera transformations
        translate(width / 2 + camX - (cols * scl) / 2, height / 2 + camY, zoom);
        rotateX(rotX);
        
        // Terrain offset moves faster with amplitude changes
        terrainOffset += 0.05 + amplitude * 0.5;

        // Generate the terrain with modulation based on the amplitude
        generateTerrain(amplitude);
        
        // Draw the modulated terrain
        drawTerrain(amplitude);
        
        // Earthquake effect duration handling
        if (earthquakeEffectDuration > 0) {
            earthquakeEffectDuration--; // Decrement the earthquake effect duration
            if (earthquakeEffectDuration <= 0) {
                regenerateTerrain = true; // Allow terrain regeneration after earthquake effects
            }
        }
    
        terrainOffset += 0.001; // Increment the terrain offset for continuous terrain movement effect

        // Update and display particles
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.update();
            p.display(this);
            if (p.isDead()) {
                particles.remove(i);
                particles.add(new Particle(this)); // Replace dead particle with a new one
                drawFog(); // Draw fog when a particle dies
            }
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
        fill(255, 255, 255, 100); // White fog with partial transparency
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
        loadAudio("fog.mp3"); // Load the "fog.mp3" file
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
