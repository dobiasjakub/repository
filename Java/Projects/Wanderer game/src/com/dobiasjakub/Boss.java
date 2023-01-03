package com.dobiasjakub;

public class Boss extends Monster{

    public Boss() {
        setHP(throwCube() *2 * getLevel());
        setDP(getLevel()* throwCube() );
        setSP(getLevel()*throwCube());
        setRegNumber(6);
    }
}
