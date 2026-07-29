package com.guildmaster.guild;

import com.guildmaster.exception.CharacterNotFoundException;
import com.guildmaster.model.Character;
import com.guildmaster.quest.Quest;
import com.guildmaster.quest.QuestResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/** Représente la guilde : ses membres, son historique et la résolution des quêtes. */
public class Guild {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final Map<String, Character> members = new LinkedHashMap<>();
    private final List<String> history = new ArrayList<>();
    private final List<QuestResult> questResults = new ArrayList<>();
    private final Random random = new Random();

    public void recruit(Character character) {
        members.put(character.getId(), character);
        log("Recrutement : " + character.getName() + " (" + character.getClassName() + ")");
    }

    public Character getCharacter(String id) throws CharacterNotFoundException {
        Character c = members.get(id);
        if (c == null) {
            throw new CharacterNotFoundException("Aucun personnage avec l'identifiant : " + id);
        }
        return c;
    }

    public void remove(String id) throws CharacterNotFoundException {
        Character c = getCharacter(id);
        members.remove(id);
        log("Départ : " + c.getName());
    }

    public List<Character> getAllCharacters() {
        return new ArrayList<>(members.values());
    }

    /**
     * Résout une quête pour une équipe donnée : compare la puissance de l'équipe
     * à la difficulté, avec une part d'aléatoire. Distribue l'expérience et l'or
     * en cas de succès, et enregistre le résultat dans l'historique.
     */
    public QuestResult resolveQuest(Quest quest, Team team) {
        int teamPower = team.getTotalPower();
        int requiredPower = quest.getDifficulty() * 20;
        // Part d'aléatoire : +/- 25% de variation sur la puissance effective
        double randomFactor = 0.75 + random.nextDouble() * 0.5;
        double effectivePower = teamPower * randomFactor;
        boolean success = effectivePower >= requiredPower;

        if (success) {
            int xpShare = quest.getExperienceReward() / team.getMembers().size();
            int goldShare = quest.getGoldReward() / team.getMembers().size();
            for (Character member : team.getMembers()) {
                boolean leveledUp = member.gainExperience(xpShare);
                member.addGold(goldShare);
                if (leveledUp) {
                    log("Montée de niveau : " + member.getName() + " atteint le niveau " + member.getLevel());
                }
            }
            log("Quête réussie : " + quest.getName() + " (puissance équipe=" + teamPower + ")");
        } else {
            log("Quête échouée : " + quest.getName() + " (puissance équipe=" + teamPower + ")");
        }

        QuestResult result = new QuestResult(quest, success, teamPower);
        questResults.add(result);
        return result;
    }

    public List<QuestResult> getQuestResults() {
        return new ArrayList<>(questResults);
    }

    public List<String> getHistory() {
        return new ArrayList<>(history);
    }

    public void log(String event) {
        history.add("[" + LocalDateTime.now().format(FORMAT) + "] " + event);
    }
}
