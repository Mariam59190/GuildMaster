package com.guildmaster.interfaces;

import com.guildmaster.model.Character;

/**
 * Contrat commun à toute entité capable de combattre.
 */
public interface Combatant {

    /**
     * Attaque une cible et lui inflige des dégâts.
     * @param target le personnage visé
     */
    void attack(Character target);

    /**
     * @return true si l'entité est encore en vie (PV > 0)
     */
    boolean isAlive();
}
