package com.dobiasjakub;

import java.util.Random;

public class Hero extends Character {
    private int maxHP;

    Hero() {
        super.setHP(20 +(3 * throwCube()));
        this.maxHP = getHP();
        super.setDP(2 * throwCube());
        super.setSP(5 + throwCube());
    }

    public void leveling(){

    }

    public void strike() {

    }

    @Override
    public String toString() {
        return "Hero (" + getLevel() + " level) HP: " + getHP() +"/" + maxHP + " | DP: "+ getDP() +" | SP:" + getSP() ;
    }
}
