package com.guildmaster.equipment;

public class Weapon implements Equipment {
    private final String name;
    private final int damageBonus;

    public Weapon(String name, int damageBonus) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Le nom de l'arme ne peut pas être vide");
        }
        if (damageBonus < 0) {
            throw new IllegalArgumentException("Le bonus de dégâts ne peut pas être négatif");
        }
        this.name = name;
        this.damageBonus = damageBonus;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPowerBonus() {
        return damageBonus;
    }

    @Override
    public String toString() {
        return "Arme[" + name + ", +" + damageBonus + " dégâts]";
    }
}
