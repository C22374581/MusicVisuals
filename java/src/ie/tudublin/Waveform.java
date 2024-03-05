package ie.tudublin;

import ddf.minim.*;
import processing.core.*;

public class Waveform {
    Visual visual; // Reference to the Visual class
    AudioPlayer player;

    public Waveform(Visual visual, AudioPlayer player) {
        this.visual = visual;
        this.player = player;
    }

    public void display() {
        visual.stroke(255);
        for (int i = 0; i < player.bufferSize() - 1; i++) {
            float x1 = PApplet.map(i, 0, player.bufferSize(), 0, visual.width);
            float x2 = PApplet.map(i + 1, 0, player.bufferSize(), 0, visual.width);
            visual.line(x1, 50 + player.left.get(i) * 50, x2, 50 + player.left.get(i + 1) * 50);
            visual.line(x1, 150 + player.right.get(i) * 50, x2, 150 + player.right.get(i + 1) * 50);
        }
    }
}
