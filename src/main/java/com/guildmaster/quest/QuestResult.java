package com.guildmaster.quest;

import java.time.LocalDateTime;

/** Résultat de la résolution d'une quête, utilisé pour l'historique et les statistiques. */
public class QuestResult {
    private final Quest quest;
    private final boolean success;
    private final int teamPower;
    private final LocalDateTime timestamp;

    public QuestResult(Quest quest, boolean success, int teamPower) {
        this.quest = quest;
        this.success = success;
        this.teamPower = teamPower;
        this.timestamp = LocalDateTime.now();
    }

    public Quest getQuest() {
        return quest;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getTeamPower() {
        return teamPower;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
