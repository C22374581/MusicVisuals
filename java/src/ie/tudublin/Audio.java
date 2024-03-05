package ie.tudublin;

import ddf.minim.*;
import processing.core.*;

public class Audio {
    Visual visual; // Reference to the Visual class
    Minim minim; // Add this line
    AudioPlayer player;

    public Audio(Visual visual) {
        this.visual = visual;
        minim = visual.getMinim(); // Now this line should work
        player = minim.loadFile(visual.dataPath("heroplanet.mp3"), visual.getFrameSize());
        player.play();
    }

    public AudioPlayer getPlayer() {
        return player;
    }
}