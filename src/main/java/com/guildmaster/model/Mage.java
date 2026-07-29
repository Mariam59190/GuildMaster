package com.guildmaster.model;

import com.guildmaster.exception.InvalidCharacterException;

/** Le Mage lance un sort qui inflige des dégâts magiques amplifiés. */
public class Mage extends Character {

    public Mage(String name, int level) throws InvalidCharacterException {
        super(name, level, 80);
    }

    @Override
    public void performAction(Character target) {
        int damage = (int) (getPower() * 1.5);
        target.takeDamage(damage);
    }

    @Override
    public String getClassName() {
        return "Mage";
    }
}
