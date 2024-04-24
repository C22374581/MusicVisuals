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

# How it works
 Inspiration and Song Choice
For this project, our team selected a diverse range of songs, each bringing a unique flavour and emotional backdrop, enabling us to explore a variety of visual narratives. The chosen tracks are:
- "The Foggy Dew" by Sinéad O'Connor & The Chieftains:
  This song, with its haunting vocals and stirring lyrics, captures the sombre and reflective mood of Irish history. The melancholic melody and the profound narrative of the lyrics provide a rich foundation for creating visuals that are evocative and deeply moving, using muted colour palettes and slow, flowing movements.
- "Thunderstruck" by AC/DC:
  Known for its high energy and electrifying guitar riffs, "Thunderstruck" offers a dynamic auditory experience that is ideal for exploring aggressive visual transitions and vibrant, pulsating effects. The intensity of the song allows for the use of stark contrasts in colours and rapid movements in the visual representation.
- "Tunnel Vision" by Kodak Black:
  This track provides a modern rhythmic base and a deep, resonant vibe that can be visually interpreted with abstract patterns and stark, minimalist designs. The song's themes of focus and perseverance are expressed through progressive visual tightening and tunnel-like visual sequences that mimic the song's deep bass and steady tempo.
- "Have You Ever Seen the Rain" by Creedence Clearwater Revival:
  This classic rock song combines a sense of nostalgia with a reflective tone, suitable for creating visuals that include natural elements, gentle movements, and a warm, comforting colour scheme. The lyrical inquiry about renewal and change can be depicted through visual cycles of rain, growth, and sunshine.
Each song inspires distinct visual elements and interactions, chosen to resonate with the song’s thematic elements and musical dynamics. This variety allows us to experiment with different styles and techniques in visual storytelling, thereby enhancing our creative scope and technical prowess in syncing visuals with varied auditory cues.



# What I am most proud of in the assignment
- Technical Integration: One of the major successes of the project was the effective integration of real-time audio processing with dynamic visual effects. We were able to implement complex audio analysis techniques such as FFT and amplitude detection to drive the visual elements, creating a synchronized and immersive experience that was highly responsive to the music.
- Abstract Artistic Expression: The project excelled in translating the emotional and thematic content of different songs into abstract visual narratives. Each song invoked a unique visual style and mood, from the gentle fall of rain and snow to the dramatic effects of thunderstorms and terrain changes, showcasing our ability to adapt visual responses to varied auditory inputs.
- Interactivity and Engagement: Enhancing user engagement through interactive elements was a key focus and success of the project. Users could manipulate visual elements through a GUI and explore the 3D visual space with camera controls, making the experience more engaging and personalized.
- Collaborative Development: The project was a testament to effective teamwork and collaboration. Using GitHub for version control and project management, we maintained a cohesive development process, where each team member’s contributions were clearly documented and integrated seamlessly.


# Markdown Tutorial

This is *emphasis*

This is a bulleted list

- Item
- Item

This is a numbered list

1. Item
1. Item

This is a [hyperlink](http://bryanduggan.org)

# Headings
## Headings
#### Headings
##### Headings

This is code:

```Java
public void render()
{
	ui.noFill();
	ui.stroke(255);
	ui.rect(x, y, width, height);
	ui.textAlign(PApplet.CENTER, PApplet.CENTER);
	ui.text(text, x + width * 0.5f, y + height * 0.5f);
}
```

So is this without specifying the language:

```
public void render()
{
	ui.noFill();
	ui.stroke(255);
	ui.rect(x, y, width, height);
	ui.textAlign(PApplet.CENTER, PApplet.CENTER);
	ui.text(text, x + width * 0.5f, y + height * 0.5f);
}
```

This is an image using a relative URL:
![image](https://github.com/C22374581/MusicVisuals/assets/124157320/c600c05f-0a22-4ef9-a3b4-0fee44da28b0)


This is an image using an absolute URL:

![image](https://github.com/C22374581/MusicVisuals/assets/124157320/fc9a7da4-55dd-45b4-b007-eef5acbf8620)


This is an image using a relative URL:
![image](https://github.com/C22374581/MusicVisuals/assets/124157320/075d4ec7-a305-48f1-980b-43a12f1baa3e)


This is a youtube video:

[![YouTube](http://img.youtube.com/vi/J2kHSSFA4NU/0.jpg)](https://www.youtube.com/watch?v=J2kHSSFA4NU)

This is a table:

| Heading 1 | Heading 2 |
|-----------|-----------|
|Some stuff | Some more stuff in this column |
|Some stuff | Some more stuff in this column |
|Some stuff | Some more stuff in this column |
|Some stuff | Some more stuff in this column |

