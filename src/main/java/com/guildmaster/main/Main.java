package com.guildmaster.main;

import com.guildmaster.exception.CharacterNotFoundException;
import com.guildmaster.exception.CorruptedSaveFileException;
import com.guildmaster.exception.InvalidCharacterException;
import com.guildmaster.exception.InvalidTeamException;
import com.guildmaster.guild.Guild;
import com.guildmaster.guild.Team;
import com.guildmaster.model.Character;
import com.guildmaster.model.Healer;
import com.guildmaster.model.Mage;
import com.guildmaster.model.Warrior;
import com.guildmaster.persistence.GuildPersistence;
import com.guildmaster.quest.Quest;
import com.guildmaster.quest.QuestResult;
import com.guildmaster.statistics.GuildStatistics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final String DATA_DIR = "data";
    private final Scanner scanner = new Scanner(System.in);
    private final GuildPersistence persistence = new GuildPersistence(DATA_DIR);
    private Guild guild;

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        loadGuild();
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> recruitCharacter();
                case "2" -> listCharacters();
                case "3" -> createTeamAndSendQuest();
                case "4" -> showHistory();
                case "5" -> showStatistics();
                case "6" -> {
                    saveAndQuit();
                    running = false;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void loadGuild() {
        try {
            guild = persistence.load();
            System.out.println("Guilde chargée (" + guild.getAllCharacters().size() + " membres).");
        } catch (CorruptedSaveFileException e) {
            System.out.println("Attention : fichier de sauvegarde corrompu (" + e.getMessage() + ").");
            System.out.println("Démarrage avec une guilde vide.");
            guild = new Guild();
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("===== GuildMaster =====");
        System.out.println("1. Recruter un personnage");
        System.out.println("2. Lister les personnages");
        System.out.println("3. Créer une équipe et l'envoyer en quête");
        System.out.println("4. Afficher l'historique de la guilde");
        System.out.println("5. Afficher les statistiques");
        System.out.println("6. Sauvegarder et quitter");
        System.out.print("Votre choix : ");
    }

    private void recruitCharacter() {
        System.out.print("Nom du personnage : ");
        String name = scanner.nextLine().trim();
        System.out.print("Classe (1=Guerrier, 2=Mage, 3=Soigneur) : ");
        String classChoice = scanner.nextLine().trim();
        System.out.print("Niveau de départ (1 par défaut) : ");
        String levelStr = scanner.nextLine().trim();
        int level = levelStr.isBlank() ? 1 : Integer.parseInt(levelStr);

        try {
            Character character = switch (classChoice) {
                case "1" -> new Warrior(name, level);
                case "2" -> new Mage(name, level);
                case "3" -> new Healer(name, level);
                default -> null;
            };
            if (character == null) {
                System.out.println("Classe inconnue.");
                return;
            }
            guild.recruit(character);
            System.out.println("Recruté : " + character);
        } catch (InvalidCharacterException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void listCharacters() {
        List<Character> all = guild.getAllCharacters();
        if (all.isEmpty()) {
            System.out.println("Aucun personnage recruté.");
            return;
        }
        System.out.print("Trier par niveau ? (o/n) : ");
        String sortChoice = scanner.nextLine().trim();
        List<Character> toShow = new ArrayList<>(all);
        if (sortChoice.equalsIgnoreCase("o")) {
            toShow.sort(Comparator.comparingInt(Character::getLevel).reversed());
        }
        for (Character c : toShow) {
            System.out.println("  [" + c.getId().substring(0, 8) + "] " + c);
        }
    }

    private void createTeamAndSendQuest() {
        List<Character> all = guild.getAllCharacters();
        if (all.size() < Team.MIN_SIZE) {
            System.out.println("Il faut au moins " + Team.MIN_SIZE + " personnages recrutés.");
            return;
        }
        listCharacters();
        System.out.println("Entrez les identifiants (8 premiers caractères) séparés par des virgules (3 à 5 membres) :");
        String input = scanner.nextLine().trim();
        String[] shortIds = input.split(",");

        try {
            List<Character> selected = new ArrayList<>();
            for (String shortId : shortIds) {
                String trimmed = shortId.trim();
                Character found = all.stream()
                        .filter(c -> c.getId().startsWith(trimmed))
                        .findFirst()
                        .orElseThrow(() -> new CharacterNotFoundException("Identifiant inconnu : " + trimmed));
                selected.add(found);
            }
            Team team = new Team(selected);

            System.out.print("Nom de la quête : ");
            String questName = scanner.nextLine().trim();
            System.out.print("Difficulté (1-10) : ");
            int difficulty = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Récompense en or : ");
            int gold = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Récompense en expérience : ");
            int xp = Integer.parseInt(scanner.nextLine().trim());

            Quest quest = new Quest(questName, difficulty, gold, xp);
            QuestResult result = guild.resolveQuest(quest, team);
            System.out.println(result.isSuccess() ? "Quête réussie !" : "Quête échouée...");

        } catch (InvalidTeamException | CharacterNotFoundException e) {
            System.out.println("Erreur : " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Valeur numérique invalide.");
        }
    }

    private void showHistory() {
        List<String> history = guild.getHistory();
        if (history.isEmpty()) {
            System.out.println("Historique vide.");
            return;
        }
        history.forEach(System.out::println);
    }

    private void showStatistics() {
        GuildStatistics stats = new GuildStatistics(guild.getAllCharacters(), guild.getQuestResults());
        System.out.println("--- Statistiques ---");
        System.out.printf("Niveau moyen : %.2f%n", stats.getAverageLevel());

        Optional<Character> mostExperienced = stats.getMostExperienced();
        System.out.println("Personnage le plus expérimenté : " +
                mostExperienced.map(Character::getName).orElse("N/A"));

        Map<String, Long> distribution = stats.getDistributionByClass();
        System.out.println("Répartition par classe : " + distribution);

        System.out.println("Top 3 les plus riches :");
        stats.getTop3Richest().forEach(c -> System.out.println("  " + c.getName() + " (" + c.getGold() + " or)"));

        System.out.println("Top 3 les plus puissants :");
        stats.getTop3Powerful().forEach(c -> System.out.println("  " + c.getName() + " (puissance " + c.getPower() + ")"));

        System.out.printf("Taux de réussite des quêtes : %.1f%%%n", stats.getQuestSuccessRate() * 100);
    }

    private void saveAndQuit() {
        try {
            persistence.save(guild);
            System.out.println("Guilde sauvegardée. À bientôt !");
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }
}
