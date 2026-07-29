package com.guildmaster.model;

import com.guildmaster.exception.InvalidCharacterException;

/**
 * Le Soigneur soigne sa cible plutôt que de lui infliger des dégâts.
 * Son "attaque" (via l'interface Combatant) est donc un soin.
 */
public class Healer extends Character {

    public Healer(String name, int level) throws InvalidCharacterException {
        super(name, level, 90);
    }

    @Override
    public void performAction(Character target) {
        int healAmount = getPower() / 2;
        target.heal(healAmount);
    }

    @Override
    public String getClassName() {
        return "Soigneur";
    }
}
