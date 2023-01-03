package com.dobiasjakub;

import java.awt.*;

public class Map {
    private int[][] map = new int[][]{
            {0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1},
            {0, 0, 0, 3, 0, 1, 0, 0, 0, 0, 1},
            {0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0},
            {0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 1},
            {0, 6, 0, 0, 1, 0, 0, 1, 0, 0, 1},
            {0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1},
            {1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1},
            };

    private boolean isFloor;

    public int checkFloor(int[][] map, int x, int y) {
        int value =0;
        if (map[y/72][x/72]<3) {
            value = 2;
            map[y/72][x/72]+= value;
        }
        return value;
    }

    Map() {
        int[][] map = this.map;

    }

    public int[][] getMap() {
        return map;
    }

    public boolean isFloor () {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == 0) {
                    this.isFloor = true;
                } else {
                    isFloor = false;
                }
            }
        } return isFloor;
    }

    public void drawMap(Graphics graphics) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                    PositionedImage floor = new PositionedImage("img/floor.png", i*72, j*72);
                    floor.draw(graphics);
                 if (map[j][i]==1) {
                    PositionedImage wall = new PositionedImage("img/wall.png", i * 72, j * 72);
                    wall.draw(graphics);
                } else if (map[j][i]>=3 && map[j][i]<=5) {
                    PositionedImage wall = new PositionedImage("img/skeleton.png", i * 72, j * 72);
                    wall.draw(graphics);
                } else if (map[j][i]==6) {
                    PositionedImage wall = new PositionedImage("img/boss.png", i*72, j*72);
                    wall.draw(graphics);
               }
            }
        }
    }
}
