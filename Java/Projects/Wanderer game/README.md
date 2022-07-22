# Wanderer - The RPG game

Build a hero based walking on tiles and killing monsters type of game. The hero
is controlled in a maze using the keyboard. Heroes and monsters have levels and
stats depending on their levels. The goal is reach the highest level by killing
the monsters holding the keys to the next level.

#### How to launch the program

- Launching the game is running the `Board` class' `main()` method.

- When reading through the specification and the stories again keep this in
  mind.

- Here's an example, it contains

  - a big drawable canvas with one image painted on it
  - and handling pressing keys, for moving your hero around
  - be aware that these are just all the needed concepts put in one place
  - you can separate anything anyhow

```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Board extends JComponent implements KeyListener {

  int testBoxX;
  int testBoxY;

  public Board() {
    testBoxX = 300;
    testBoxY = 300;

    // set the size of your draw board
    setPreferredSize(new Dimension(720, 720));
    setVisible(true);
  }

  @Override
  public void paint(Graphics graphics) {
    super.paint(graphics);
    graphics.fillRect(testBoxX, testBoxY, 100, 100);
    // here you have a 720x720 canvas
    // you can create and draw an image using the class below e.g.
    PositionedImage image = new PositionedImage("yourimage.png", 300, 300);
    image.draw(graphics);
  }

  public static void main(String[] args) {
    // Here is how you set up a new window and adding our board to it
    JFrame frame = new JFrame("RPG Game");
    Board board = new Board();
    frame.add(board);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    frame.pack();
    // Here is how you can add a key event listener
    // The board object will be notified when hitting any key
    // with the system calling one of the below 3 methods
    frame.addKeyListener(board);
    // Notice (at the top) that we can only do this
    // because this Board class (the type of the board object) is also a KeyListener
  }

  // To be a KeyListener the class needs to have these 3 methods in it
  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {

  }

  // But actually we can use just this one for our goals here
  @Override
  public void keyReleased(KeyEvent e) {
    // When the up or down keys hit, we change the position of our box
    if (e.getKeyCode() == KeyEvent.VK_UP) {
      testBoxY -= 100;
    } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
      testBoxY += 100;
    }
    // and redraw to have a new picture with the new coordinates
    repaint();
  
  }

}

```

- Image class as a base:

```java
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PositionedImage {

  BufferedImage image;
  int posX, posY;

  public PositionedImage(String filename, int posX, int posY) {
    this.posX = posX;
    this.posY = posY;
    try {
      image = ImageIO.read(new File(filename));
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public void draw(Graphics graphics) {
    if (image != null) {
      graphics.drawImage(image, posX, posY, null);
    }

  }

}

```
# Introduction

*Stories should be followed by implementation. Completing a story means you made
a little progress on the project.*

*Keep in mind, that you have to develop the complete game. So when working on a
specific story, you should be able to reuse the implementation later in other
parts of the game.*

## Draw a screen with tiles

### 1. Draw a single tile

- Given the launched game
- Then it should show a tile like this:

| Floor tile                  |
| --------------------------- |
| ![floor.png](img/floor.png) |

### 2. Fill the screen with the tile

- Given the launched game
- Then it should show a map of tiles like this:

![floor map](img/floor-map.png)

### 3. Add wall tiles

- Given the launched game
- When the map is rendered on the screen
- Then it should show floor and wall type tiles as well like on this layout (you
  can arrange wall differently if you wish):

| Floor tile                  | Wall tile                 |
| --------------------------- | ------------------------- |
| ![floor.png](img/floor.png) | ![wall.png](img/wall.png) |

![full map](img/full-map.png)

## Place a character on it and move with key bindings

### 4. Add the Hero

- Given the launched game
- When the map is rendered on the screen
- Add the player character called the hero
- Then it should show a hero on the top-left corner:

| Hero                       |
| -------------------------- |
| ![hero](img/hero-down.png) |

![hero map](img/hero-map.png)

## Interactions

The player should be able to move the hero by using their arrow keys.

### 5. Move around

- Given the launched game
- When *any* of the arrow keys are pressed by the user
- Then the hero should move to that direction

### 6. Hero direction

- Given the launched game
- When the hero is moved by the arrow keys
- Then the hero should face the direction where he went

| Hero Up                | Hero Right                   | Hero Down                  | Hero Left                  |
| ---------------------- | ---------------------------- | -------------------------- | -------------------------- |
| ![up](img/hero-up.png) | ![right](img/hero-right.png) | ![down](img/hero-down.png) | ![left](img/hero-left.png) |

### 7. Map boundaries

- Given the hero on any edge of the map
- When the hero is moved by the arrow keys towards the edge
- Then it should not move or leave the map, only its direction should change if
  necessary

### 8. Walls

- Given the hero next to a wall tile
- When the hero is moved by the arrow keys towards the wall tile
- Then it should not move, only its direction should change if necessary

## Extend with different kinds of characters

### 9. Skeletons

- Given the launched game
- When the map is rendered on the screen
- Then 3 skeletons should be on the map, somewhere on floor type tiles

| Skeleton                          |
| --------------------------------- |
| ![skeleton.png](img/skeleton.png) |

### 10. Boss

- Given the launched game
- When the map is rendered on the screen
- Then a boss should be on the map, somewhere on floor type tiles

| Boss                      |
| ------------------------- |
| ![boss.png](img/boss.png) |

## Create HUD, fight & game logic

### 11. Stats

- Given the launched game
- When the map is rendered on the screen
- Then stats should appear below the map in a white box as black strings
  - It should contain:
    - The level of the Hero
    - The current HP (*health point*) of the Hero
    - The max HP of the Hero
    - The DP (*defend point*) of the Hero
    - The SP (*strike point*) of the Hero
  - Like this: `Hero (Level 1) HP: 8/10 | DP: 8 | SP: 6`

### 12. Strike

- Characters are able to strike as detailed in the
  [specification](/project/wanderer#strike)

### 13. Battle logic

- After a hero character performed a strike the defender should strike back the
  same way

### 14. Next area

- When the enemy with the key is killed, the hero should enter the new level
  automatically

## Optional features

### 15. Update characters on moving hero

- The characters should only move when the player moves the hero

### 16. Random map

- When map is created the placement of walls should be random
- Make sure that all floor tiles are connected

### 17. Leveling

- Add more hp / damage to the hero according to the specification
- Add more hp / damage to the monsters
- Implement random events which happen when entering the new area

### 18. Monsters moving around

- Move the monsters around regardless of player moving hero or not
- Speed up their movement level by level

## TODO
- Implement battle logic
- Implement leveling
