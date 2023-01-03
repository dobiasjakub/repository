package com.dobiasjakub;

public class Skeleton extends Monster {

    public Skeleton(int regNumber) {
        this.setRegNumber(regNumber);
        setHP(throwCube() *2 * getLevel());
        setDP((getLevel() /2)* throwCube() );
        setSP(getLevel()*throwCube());
    }

}
