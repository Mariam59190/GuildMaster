package com.guildmaster.model;

import com.guildmaster.equipment.Equipment;
import com.guildmaster.exception.InvalidCharacterException;
import com.guildmaster.interfaces.Combatant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Classe abstraite représentant un aventurier de la guilde.
 * Toute sous-classe doit définir son type de combattant
 * (attaque, sort, soin...) via {@link #performAction(Character)}.
 */
public abstract class Character implements Combatant {

    private final String id;
    private String name;
    private int level;
    private int healthPoints;
    private int maxHealthPoints;
    private int experience;
    private int gold;
    private final List<Equipment> inventory = new ArrayList<>();

    protected Character(String name, int level, int healthPoints) throws InvalidCharacterException {
        if (name == null || name.isBlank()) {
            throw new InvalidCharacterException("Le nom du personnage ne peut pas être vide");
        }
        if (level < 1) {
            throw new InvalidCharacterException("Le niveau doit être supérieur ou égal à 1");
        }
        if (healthPoints <= 0) {
            throw new InvalidCharacterException("Les points de vie doivent être strictement positifs");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.level = level;
        this.maxHealthPoints = healthPoints;
        this.healthPoints = healthPoints;
        this.experience = 0;
        this.gold = 0;
    }

    /** Action de combat spécifique à la sous-classe (attaque, sort, soin...). */
    public abstract void performAction(Character target);

    /** Nom de la classe de personnage (Guerrier, Mage, Soigneur...) affiché à l'utilisateur. */
    public abstract String getClassName();

    @Override
    public void attack(Character target) {
        performAction(target);
    }

    @Override
    public boolean isAlive() {
        return healthPoints > 0;
    }

    public void takeDamage(int amount) {
        if (amount < 0) {
            amount = 0;
        }
        this.healthPoints = Math.max(0, this.healthPoints - amount);
    }

    public void heal(int amount) {
        if (amount < 0) {
            amount = 0;
        }
        this.healthPoints = Math.min(maxHealthPoints, this.healthPoints + amount);
    }

    /** Calcule la puissance de combat du personnage : niveau + équipements. */
    public int getPower() {
        int equipmentBonus = inventory.stream().mapToInt(Equipment::getPowerBonus).sum();
        return level * 10 + equipmentBonus;
    }

    /** Ajoute de l'expérience et fait monter de niveau si le seuil est atteint. Renvoie true si level up. */
    public boolean gainExperience(int amount) {
        if (amount < 0) {
            return false;
        }
        this.experience += amount;
        int threshold = level * 100;
        boolean leveledUp = false;
        while (this.experience >= threshold) {
            this.experience -= threshold;
            this.level++;
            this.maxHealthPoints += 10;
            this.healthPoints = maxHealthPoints;
            leveledUp = true;
            threshold = level * 100;
        }
        return leveledUp;
    }

    public void addGold(int amount) {
        if (amount > 0) {
            this.gold += amount;
        }
    }

    public void equip(Equipment equipment) {
        if (equipment != null) {
            inventory.add(equipment);
        }
    }

    public List<Equipment> getInventory() {
        return new ArrayList<>(inventory);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public int getExperience() {
        return experience;
    }

    public int getGold() {
        return gold;
    }

    @Override
    public String toString() {
        return String.format("%s [%s] - Niv.%d - PV:%d/%d - XP:%d - Or:%d",
                name, getClassName(), level, healthPoints, maxHealthPoints, experience, gold);
    }
}
