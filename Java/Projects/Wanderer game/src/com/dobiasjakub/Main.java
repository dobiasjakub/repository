package com.dobiasjakub;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Main extends JComponent implements KeyListener {

    Map map = new Map();
    int[][] map1 = map.getMap();
    Monster monster = new Monster();
    Hero hero = new Hero();

    int testBoxX;
    int testBoxY;
    String addressOfHeroMovement = "C:\\Users\\dobia\\OneDrive\\Plocha\\wanderer-java\\img\\hero-down.png";
    int counter = 0;

    public Main() {
        testBoxX = 0;
        testBoxY = 0;

        Monster monster = new Monster();
        monster.addMonster(new Skeleton(3));
        monster.addMonster(new Skeleton(4));
        monster.addMonster(new Skeleton(5));
        monster.addMonster(new Boss());

        // set the size of your draw board
        setPreferredSize(new Dimension(792, 792));
        setVisible(true);
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        // here you have a 720x720 canvas
        // you can create and draw an image using the class below e.g.
        map.drawMap(graphics);
        paintHero(graphics,testBoxX,testBoxY);
        drawHeroStats(graphics,hero.toString());
    }

    public void drawHeroStats(Graphics graphics, String text) {
        if (text!=null) {
            Font font = new Font("Serif", Font.BOLD, 20);

            graphics.setFont(font);
            graphics.setColor(Color.WHITE);
            graphics.drawString(text,450,760);
        }
    }

    public void paintHero(Graphics graphics, int testBoxX, int testBoxY) {
        PositionedImage heroUp = new PositionedImage(addressOfHeroMovement, testBoxX, testBoxY);
        heroUp.draw(graphics);
        }

    public void moveEnemy() {
        HashMap<Integer,Boolean> movement = new HashMap<>();
        movement.put(3,true);
        movement.put(4,true);
        movement.put(5,true);
        movement.put(6,true);

        for (int i = 0; i < map1.length; i++) {
            for (int j = 0; j < map1.length; j++) {
                if (map1[i][j] > 2 ){
                    int number = map1[i][j];
                    if (movement.get(number)) {
                        Position freePos = new Position();
                        Position finalPos = freePos.checkPositions(map1, i, j);
                        map1[i][j] = 0;
                        map1[finalPos.positionX][finalPos.positionY] = number;
                        movement.put(number,false);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        // Here is how you set up a new window and adding our board to it
        JFrame frame = new JFrame("RPG Game");
        Main board = new Main();
        frame.add(board);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();

        Monster monster = new Monster();
        monster.addMonster(new Skeleton(3));
        monster.addMonster(new Skeleton(4));
        monster.addMonster(new Skeleton(5));
        monster.addMonster(new Boss());

        // Here is how you can add a key event listener
        // The board object will be notified when hitting any key
        // with the system calling one of the below 3 methods
        frame.addKeyListener(board);
        // Notice (at the top) that we can only do this
        // because this Main class (the type of the board object) is also a KeyListener
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
    public void keyReleased(KeyEvent e) throws ArrayIndexOutOfBoundsException {
        // When the up or down keys hit, we change the position of our box

            if (e.getKeyCode() == KeyEvent.VK_UP) {
                addressOfHeroMovement = "img/hero-up.png";
                if ((map1[testBoxY/72-1][testBoxX/72]!=1) && (testBoxY/72  != 0)) {
                    if (map1[testBoxY/72][testBoxX/72]<3) {
                        map1[testBoxY / 72][testBoxX / 72]=0;
                    }
                    testBoxY -= 72;
                    map1[testBoxY/72][testBoxX/72]+=2;
                    counter++;
                }

            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                addressOfHeroMovement = "img/hero-down.png";
                if ((map1[testBoxY/72+1][testBoxX/72]!=1)  && ((testBoxY + 72) <=792)){
                    if (map1[testBoxY/72][testBoxX/72]<3) {
                        map1[testBoxY / 72][testBoxX / 72] = 0;
                    }
                    testBoxY += 72;
                    if (map1[testBoxY/72][testBoxX/72]<3) {
                        map1[testBoxY / 72][testBoxX / 72]+= 2;
                    }
                    counter++;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                addressOfHeroMovement = "img/hero-left.png";
                if ((map1[testBoxY/72][testBoxX/72-1]!=1) && ((testBoxX - 72) >= 0)){
                    if (map1[testBoxY/72][testBoxX/72]<3) {
                        map1[testBoxY / 72][testBoxX / 72] = 0;
                    }
                    testBoxX -= 72;
                    if (map1[testBoxY/72][testBoxX/72]<3) {
                        map1[testBoxY / 72][testBoxX / 72]+= 2;
                    }
                    counter++;
                }

            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                addressOfHeroMovement = "img/hero-right.png";
                if ((map1[testBoxY/72][testBoxX/72+1]!=1) && ((testBoxY + 72) <=792)) {
                    if (map1[testBoxY/72][testBoxX/72] < 3) {
                        map1[testBoxY / 72][testBoxX / 72] = 0;
                    }
                    testBoxX += 72;
                    if (map1[testBoxY/72][testBoxX/72]<  3) {
                        map1[testBoxY / 72][testBoxX / 72] += 2;
                    }
                    counter++;
            }
        }
        // and redraw to have a new picture with the new coordinates
        if (counter%2==0) {
            moveEnemy();
        }
        repaint();
    }

    public int getTestBoxX() {
        return testBoxX;
    }
}