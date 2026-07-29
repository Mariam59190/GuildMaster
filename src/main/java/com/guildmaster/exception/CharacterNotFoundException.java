package com.guildmaster.exception;

/**
 * Levée quand un identifiant de personnage ne correspond à
 * aucun membre connu de la guilde.
 */
public class CharacterNotFoundException extends Exception {
    public CharacterNotFoundException(String message) {
        super(message);
    }
}
