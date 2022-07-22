package com.dobiasjakub;

import java.util.ArrayList;
import java.util.List;

public class Monster extends Character {
    public List<Monster> monsters;

    public Monster() {
        this.monsters = new ArrayList<>();
    }

    public int getSize() {
        return this.monsters.size();
    }

    public void addMonster (Monster monster) {
        this.monsters.add(monster);
    }

    public void setKey() {
        int keyHolder = throwCube(2);
        if (monsters.get(keyHolder) instanceof Skeleton) {
            monsters.get(keyHolder).setKey();
        }
    }

    public void allMonstersHasNewMove() {
        for (Monster monster : monsters) {
            monster.setMoved(false);
        }
    }

    public List<Monster> getMonsters() {
        return this.monsters;
    }

    public Monster getMonster(int regNum) {
        for (Monster monster : monsters) {
            if (monster.getRegNum() == regNum) {
                return monster;
            }
        }
        return null;
    }

    public boolean monsterCanMove (int regNum) {
        for (Monster monster : monsters) {
            if (monster.getRegNum() == regNum) {
            return monster.isMoveAble();
            }
        } return false;
    }

    public void monsterMove (int regNum) {
        for (Monster monster : monsters) {
            if (monster.getRegNum() == regNum) {
                monster.setMoved(true);
            }
        }
    }

    public void setCoordinates(int regNum, int x, int y) {
        for (Monster monster : monsters) {
            if (monster.getRegNum() == regNum) {
                monster.setPosXY(x, y);
                monster.setMoved(true);
            }
        }
    }
}
