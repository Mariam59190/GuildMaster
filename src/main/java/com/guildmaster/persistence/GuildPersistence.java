package com.guildmaster.persistence;

import com.guildmaster.exception.CorruptedSaveFileException;
import com.guildmaster.exception.InvalidCharacterException;
import com.guildmaster.guild.Guild;
import com.guildmaster.model.Character;
import com.guildmaster.model.Healer;
import com.guildmaster.model.Mage;
import com.guildmaster.model.Warrior;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Gère la persistance de la guilde au format CSV.
 * Un fichier absent renvoie simplement une guilde vide (pas d'erreur).
 * Un fichier corrompu lève une CorruptedSaveFileException qui est
 * systématiquement interceptée par l'appelant (Main) sans faire
 * planter le programme.
 */
public class GuildPersistence {

    private static final String CHARACTERS_HEADER = "id;name;class;level;hp;maxHp;xp;gold";
    private static final String HISTORY_HEADER = "event";

    private final Path charactersFile;
    private final Path historyFile;

    public GuildPersistence(String directory) {
        this.charactersFile = Path.of(directory, "characters.csv");
        this.historyFile = Path.of(directory, "history.csv");
    }

    public void save(Guild guild) throws IOException {
        Files.createDirectories(charactersFile.getParent());

        StringBuilder charSb = new StringBuilder(CHARACTERS_HEADER).append("\n");
        for (Character c : guild.getAllCharacters()) {
            charSb.append(String.join(";",
                    c.getId(),
                    escape(c.getName()),
                    c.getClassName(),
                    String.valueOf(c.getLevel()),
                    String.valueOf(c.getHealthPoints()),
                    String.valueOf(c.getMaxHealthPoints()),
                    String.valueOf(c.getExperience()),
                    String.valueOf(c.getGold())
            )).append("\n");
        }
        Files.writeString(charactersFile, charSb.toString(), StandardCharsets.UTF_8);

        StringBuilder histSb = new StringBuilder(HISTORY_HEADER).append("\n");
        for (String event : guild.getHistory()) {
            histSb.append(escape(event)).append("\n");
        }
        Files.writeString(historyFile, histSb.toString(), StandardCharsets.UTF_8);
    }

    /**
     * Charge la guilde depuis le disque. Si les fichiers n'existent pas,
     * renvoie une guilde vide. Si un fichier existe mais est corrompu,
     * lève une CorruptedSaveFileException (à charge de l'appelant de la
     * gérer sans planter).
     */
    public Guild load() throws CorruptedSaveFileException {
        Guild guild = new Guild();

        if (Files.exists(charactersFile)) {
            loadCharacters(guild);
        }
        if (Files.exists(historyFile)) {
            loadHistory(guild);
        }
        return guild;
    }

    private void loadCharacters(Guild guild) throws CorruptedSaveFileException {
        List<String> lines;
        try {
            lines = Files.readAllLines(charactersFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CorruptedSaveFileException("Impossible de lire " + charactersFile, e);
        }

        for (int i = 1; i < lines.size(); i++) { // ligne 0 = en-tête
            String line = lines.get(i);
            if (line.isBlank()) continue;
            String[] parts = line.split(";", -1);
            if (parts.length != 8) {
                throw new CorruptedSaveFileException(
                        "Ligne corrompue dans characters.csv (colonnes attendues=8) : " + line);
            }
            try {
                String name = unescape(parts[1]);
                String className = parts[2];
                int level = Integer.parseInt(parts[3]);
                int hp = Integer.parseInt(parts[4]);
                int maxHp = Integer.parseInt(parts[5]);
                int xp = Integer.parseInt(parts[6]);
                int gold = Integer.parseInt(parts[7]);

                Character c = createCharacter(className, name, level);
                // Ajuste les PV/XP/or exacts après construction (constructeur fixe les PV max de base)
                int damage = maxHp - hp;
                if (damage > 0) c.takeDamage(damage);
                c.gainExperience(0); // no-op, garde la cohérence de l'API
                restoreExperienceAndGold(c, xp, gold);

                guild.recruit(c);
            } catch (NumberFormatException | InvalidCharacterException e) {
                throw new CorruptedSaveFileException("Donnée invalide dans characters.csv : " + line, e);
            }
        }
    }

    private void restoreExperienceAndGold(Character c, int xp, int gold) {
        c.gainExperience(xp);
        c.addGold(gold);
    }

    private Character createCharacter(String className, String name, int level) throws InvalidCharacterException, CorruptedSaveFileException {
        return switch (className) {
            case "Guerrier" -> new Warrior(name, level);
            case "Mage" -> new Mage(name, level);
            case "Soigneur" -> new Healer(name, level);
            default -> throw new CorruptedSaveFileException("Classe de personnage inconnue : " + className);
        };
    }

    private void loadHistory(Guild guild) throws CorruptedSaveFileException {
        List<String> lines;
        try {
            lines = Files.readAllLines(historyFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CorruptedSaveFileException("Impossible de lire " + historyFile, e);
        }
        for (int i = 1; i < lines.size(); i++) {
            if (!lines.get(i).isBlank()) {
                guild.log(unescape(lines.get(i)) + " (chargé)");
            }
        }
    }

    private String escape(String value) {
        return value.replace(";", ",").replace("\n", " ");
    }

    private String unescape(String value) {
        return value;
    }
}
