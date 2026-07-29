package com.guildmaster.quest;

public class Quest {
    private final String name;
    private final int difficulty; // 1 à 10
    private final int goldReward;
    private final int experienceReward;

    public Quest(String name, int difficulty, int goldReward, int experienceReward) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Le nom de la quête ne peut pas être vide");
        }
        if (difficulty < 1 || difficulty > 10) {
            throw new IllegalArgumentException("La difficulté doit être comprise entre 1 et 10");
        }
        this.name = name;
        this.difficulty = difficulty;
        this.goldReward = goldReward;
        this.experienceReward = experienceReward;
    }

    public String getName() {
        return name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getGoldReward() {
        return goldReward;
    }

    public int getExperienceReward() {
        return experienceReward;
    }

    @Override
    public String toString() {
        return name + " (difficulté " + difficulty + "/10, récompense " + goldReward + " or / " + experienceReward + " xp)";
    }
}
