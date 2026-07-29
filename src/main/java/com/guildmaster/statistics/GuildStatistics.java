package com.guildmaster.statistics;

import com.guildmaster.model.Character;
import com.guildmaster.quest.QuestResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/** Regroupe tous les traitements statistiques de la guilde, réalisés avec l'API Stream. */
public class GuildStatistics {

    private final List<Character> characters;
    private final List<QuestResult> questResults;

    public GuildStatistics(List<Character> characters, List<QuestResult> questResults) {
        this.characters = characters;
        this.questResults = questResults;
    }

    /** Niveau moyen de la guilde. */
    public double getAverageLevel() {
        return characters.stream()
                .mapToInt(Character::getLevel)
                .average()
                .orElse(0.0);
    }

    /** Le personnage le plus expérimenté (le plus d'XP cumulée en tenant compte du niveau). */
    public Optional<Character> getMostExperienced() {
        return characters.stream()
                .max(Comparator.comparingInt(c -> c.getLevel() * 1000 + c.getExperience()));
    }

    /** Répartition des personnages par classe. */
    public Map<String, Long> getDistributionByClass() {
        return characters.stream()
                .collect(Collectors.groupingBy(Character::getClassName, Collectors.counting()));
    }

    /** Top 3 des personnages les plus riches. */
    public List<Character> getTop3Richest() {
        return characters.stream()
                .sorted(Comparator.comparingInt(Character::getGold).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    /** Top 3 des personnages les plus puissants. */
    public List<Character> getTop3Powerful() {
        return characters.stream()
                .sorted(Comparator.comparingInt(Character::getPower).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    /** Taux de réussite des quêtes (quêtes réussies / quêtes tentées), entre 0 et 1. */
    public double getQuestSuccessRate() {
        if (questResults.isEmpty()) {
            return 0.0;
        }
        long successes = questResults.stream().filter(QuestResult::isSuccess).count();
        return (double) successes / questResults.size();
    }
}
