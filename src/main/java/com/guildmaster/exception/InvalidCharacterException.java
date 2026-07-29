package com.guildmaster.exception;

/**
 * Levée quand les données d'un personnage sont invalides
 * (nom vide, niveau négatif, points de vie invalides...).
 */
public class InvalidCharacterException extends Exception {
    public InvalidCharacterException(String message) {
        super(message);
    }
}
