package com.dobiasjakub;

import java.util.List;
import java.util.Random;

public abstract class Character {
    private int HP;
    private int DP;
    private int SP;
    private int regNumber;
    private int posX;
    private int posY;
    private boolean hasKey;
    private boolean isDead;
    private boolean moveAble;
    private int level = 1;

    public int getHP() {
        return HP;
    }

    public void setMoved(boolean moveAble){
        this.moveAble = moveAble;
        }

    public int getLevel () {
        return this.level;
    }

    public void setRegNumber (int regNumber) {
        this.regNumber = regNumber;
    }

    public int getRegNum() {
        return this.regNumber;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getDP() {
        return DP;
    }

    public void setDP(int DP) {
        this.DP = DP;
    }

    public int getSP() {
        return SP;
    }

    public void setSP(int SP) {
        this.SP = SP;
    }

    public int throwCube() {
        Random random = new Random();
        return random.nextInt(6) + 2;
    }

    public int throwCube(int bound) {
        Random random = new Random();
        return random.nextInt(6) + 2;
    }

    public void setPosXY(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public boolean isMoveAble() {
        return moveAble;
    }

    public String toString() {
        return "";
    }
}


