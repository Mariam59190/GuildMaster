package com.guildmaster.model;

import com.guildmaster.equipment.Weapon;
import com.guildmaster.exception.InvalidCharacterException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {

    @Test
    void creatingWarriorWithBlankNameThrows() {
        assertThrows(InvalidCharacterException.class, () -> new Warrior("  ", 1));
    }

    @Test
    void creatingWarriorWithInvalidLevelThrows() {
        assertThrows(InvalidCharacterException.class, () -> new Warrior("Konan", 0));
    }

    @Test
    void warriorDealsDamageOnAttack() throws InvalidCharacterException {
        Warrior attacker = new Warrior("Konan", 1);
        Mage defender = new Mage("Yugo", 1);
        int hpBefore = defender.getHealthPoints();
        attacker.attack(defender);
        assertTrue(defender.getHealthPoints() < hpBefore);
    }

    @Test
    void healerHealsInsteadOfDamaging() throws InvalidCharacterException {
        Healer healer = new Healer("Amalia", 3);
        Warrior injured = new Warrior("Konan", 3);
        injured.takeDamage(50);
        int hpBefore = injured.getHealthPoints();
        healer.attack(injured);
        assertTrue(injured.getHealthPoints() > hpBefore);
    }

    @Test
    void equippingWeaponIncreasesPower() throws InvalidCharacterException {
        Warrior warrior = new Warrior("Konan", 1);
        int powerBefore = warrior.getPower();
        warrior.equip(new Weapon("Épée longue", 15));
        assertEquals(powerBefore + 15, warrior.getPower());
    }

    @Test
    void gainingEnoughExperienceLevelsUp() throws InvalidCharacterException {
        Warrior warrior = new Warrior("Konan", 1);
        boolean leveledUp = warrior.gainExperience(150);
        assertTrue(leveledUp);
        assertEquals(2, warrior.getLevel());
    }

    @Test
    void deadCharacterIsNotAlive() throws InvalidCharacterException {
        Warrior warrior = new Warrior("Konan", 1);
        warrior.takeDamage(10_000);
        assertFalse(warrior.isAlive());
    }
}
