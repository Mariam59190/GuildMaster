package com.guildmaster.exception;

/**
 * Levée quand une équipe ne respecte pas les règles
 * (taille hors de [3,5], membre déjà mort, doublon...).
 */
public class InvalidTeamException extends Exception {
    public InvalidTeamException(String message) {
        super(message);
    }
}
