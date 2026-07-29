package com.guildmaster.exception;

/**
 * Levée quand un fichier de sauvegarde existe mais que son
 * contenu ne peut pas être interprété (ligne malformée, champ
 * manquant, valeur numérique invalide...).
 * Cette exception est toujours interceptée par la couche
 * persistence : elle ne doit jamais faire planter le programme.
 */
public class CorruptedSaveFileException extends Exception {
    public CorruptedSaveFileException(String message) {
        super(message);
    }

    public CorruptedSaveFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
