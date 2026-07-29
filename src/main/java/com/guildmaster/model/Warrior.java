package com.guildmaster.model;

import com.guildmaster.exception.InvalidCharacterException;

/** Le Guerrier inflige des dégâts physiques bruts basés sur sa puissance. */
public class Warrior extends Character {

    public Warrior(String name, int level) throws InvalidCharacterException {
        super(name, level, 120);
    }

    @Override
    public void performAction(Character target) {
        int damage = getPower();
        target.takeDamage(damage);
    }

    @Override
    public String getClassName() {
        return "Guerrier";
    }
}
