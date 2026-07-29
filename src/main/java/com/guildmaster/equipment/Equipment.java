package com.guildmaster.equipment;

/**
 * Contrat commun à tout objet équipable par un personnage.
 * Chaque équipement apporte un bonus de puissance qui vient
 * s'ajouter aux statistiques de base du porteur.
 */
public interface Equipment {

    String getName();

    /**
     * Bonus de puissance apporté par l'objet (utilisé dans le
     * calcul de la puissance de combat et de la puissance d'équipe).
     */
    int getPowerBonus();
}
