package com.dobiasjakub;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Position {

    public int positionX;
    public int positionY;
    public List<Position> freePositions;

    public Position() {
        this.freePositions = new ArrayList<>();
    }

    public Position(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public int getPositionX() {
        return positionX;
    }

    public void addPosition (int positionX, int positionY) {
        freePositions.add(new Position(this.positionX,this.positionY));
    }
    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public List<Position> getFreePositions() {
        return freePositions;
    }

    public void setFreePositions(List<Position> freePositions) {
        this.freePositions = freePositions;
    }

    public Position checkPositions (int[][] map, int x, int y) {
        List<Position> freePositions = new ArrayList<>();
        if (x > 0 && map[x-1][y] <1) {
            freePositions.add(new Position(x-1,y));
        }
        if (x < 10 && map [x+1][y] < 1) {
            freePositions.add(new Position(x+1,y));
        }
        if (y > 0 && map[x][y-1] < 1 ) {
            freePositions.add(new Position(x,y-1));
        }
        if (y < 10 && map[x][y+1] < 1) {
            freePositions.add(new Position(x, y + 1));
        } else {
            freePositions.add(new Position(x,y));
        }
        int randomPos = (int) (Math.random() * freePositions.size());
        Position finalOne = freePositions.get(randomPos);
        return finalOne;
    }

    public void moveEnemy() {
        Map map = new Map();
        int map1[][] = map.getMap();
        for (int i = 0; i < map1.length; i++) {
            for (int j = 0; j < map1.length; j++) {
                if (map1[i][j] > 2) {
                    int number = map1[i][j];
                    Position freePos = new Position();
                    Position finalo = freePos.checkPositions(map1,i,j);
                    map1[i][j]=0;
                    map1[finalo.positionX][finalo.positionY] = number;
                }
            }
        }
    }
}
