package com.guildmaster.equipment;

public class Armor implements Equipment {
    private final String name;
    private final int defenseBonus;

    public Armor(String name, int defenseBonus) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Le nom de l'armure ne peut pas être vide");
        }
        if (defenseBonus < 0) {
            throw new IllegalArgumentException("Le bonus de défense ne peut pas être négatif");
        }
        this.name = name;
        this.defenseBonus = defenseBonus;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPowerBonus() {
        return defenseBonus;
    }

    @Override
    public String toString() {
        return "Armure[" + name + ", +" + defenseBonus + " défense]";
    }
}
