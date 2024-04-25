# Music Visualiser Project

Name:Jason Glancy/ Tadhg Roche

Student Number: C22374581/c22348761

## Instructions
- Fork this repository and use it a starter project for your assignment
- Create a new package named your student number and put all your code in this package.
- You should start by creating a subclass of ie.tudublin.Visual
- There is an example visualiser called MyVisual in the example package
- Check out the WaveForm and AudioBandsVisual for examples of how to call the Processing functions from other classes that are not subclasses of PApplet

# Description of the assignment
The objective of our project is to create an abstract visual story that interprets and reflects the essence of a chosen song using Processing and its associated sound libraries, such as Minim and the Processing Sound library. This initiative seeks to explore the convergence of music and visual art, where the audio's mood, rhythm, and tempo actively influence and drive the visual output. The final creation will be an immersive experience that allows the audience to perceive music through visual expressions, highlighting the synergy between auditory and visual sensory modalities.
# Instructions
Fork this repository and use it as a starter project for your assignment.
Create a new package named with your student number and put all your code in this package.
You should start by creating a subclass of ie.tudublin.Visual.
Check out the WaveForm and AudioBandsVisual for examples of how to call the Processing functions from other classes that are not subclasses of PApplet.
# How it works

 Project Class: This class extends Visual and serves as the main entry point for the project.
Eye Class: Represents an eye object with methods for updating its position and displaying it on the screen.
Terrain Generation: Includes methods for generating and rendering terrain based on Perlin noise.
Weather Effects: Different visual effects are applied based on the current weather condition.
Background Effects: Various background effects are implemented to enhance the visual experience.
User Interaction: Users can control camera movement, zoom, and trigger special effects.
Audio Integration: Different songs can be loaded and played in the background, synchronized with visual effects.



# What I am most proud of in the assignment
- Technical Integration: One of the major successes of the project was the effective integration of real-time audio processing with dynamic visual effects. We were able to implement complex audio analysis techniques such as FFT and amplitude detection to drive the visual elements, creating a synchronized and immersive experience that was highly responsive to the music.
- Abstract Artistic Expression: The project excelled in translating the emotional and thematic content of different songs into abstract visual narratives. Each song invoked a unique visual style and mood, from the gentle fall of rain and snow to the dramatic effects of thunderstorms and terrain changes, showcasing our ability to adapt visual responses to varied auditory inputs.
- Interactivity and Engagement: Enhancing user engagement through interactive elements was a key focus and success of the project. Users could manipulate visual elements through a GUI and explore the 3D visual space with camera controls, making the experience more engaging and personalized.
- Collaborative Development: The project was a testament to effective teamwork and collaboration. Using GitHub for version control and project management, we maintained a cohesive development process, where each team member’s contributions were clearly documented and integrated seamlessly.




This is a [hyperlink](http://bryanduggan.org)


Terrain1 code:

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

Terrain2 code:
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

Terrain3 Code:
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

    // Draw small stars orbiting around the big star
    drawOrbitingStars(middleX, middleY, middleSize, time);
}
Terain4 code:
void drawTerrain4(float amplitude) {
    background(102);
    e1.update(mouseX, mouseY);
    e2.update(mouseX, mouseY);

    e1.display(this);
    e2.display(this);
    
    drawMouth(width / 2, height / 2 + 120, 140, 60); // Move the mouth further down and make it larger
}
    
void drawMouth(int x, int y, int w, int h) {
    fill(255, 0, 0); // Fill color for the mouth (red)
    noStroke(); // No border for the mouth
    arc(x, y, w, h, 0, PI); // Draw a semi-circle for the open mouth
}


      
   


   

This is an image using a relative URL:
![image](https://github.com/C22374581/MusicVisuals/assets/124157320/8a055eec-3141-45b4-8b82-4fb4632d26cd)




This is an image using an absolute URL:
![image](https://github.com/C22374581/MusicVisuals/assets/124157320/71b1eeda-2bcb-444e-8b74-9ed556c0f42f)




This is an image using a relative URL:
![image](https://github.com/C22374581/MusicVisuals/assets/124157320/c799a986-2bae-4041-8835-238447815c5e)


This is an image using a relative URL:


This is a youtube video:



