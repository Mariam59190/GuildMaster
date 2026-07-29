package com.guildmaster.statistics;

import com.guildmaster.exception.InvalidCharacterException;
import com.guildmaster.model.Character;
import com.guildmaster.model.Mage;
import com.guildmaster.model.Warrior;
import com.guildmaster.quest.Quest;
import com.guildmaster.quest.QuestResult;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GuildStatisticsTest {

    @Test
    void averageLevelIsComputedCorrectly() throws InvalidCharacterException {
        Character a = new Warrior("A", 2);
        Character b = new Warrior("B", 4);
        GuildStatistics stats = new GuildStatistics(List.of(a, b), List.of());
        assertEquals(3.0, stats.getAverageLevel());
    }

    @Test
    void distributionByClassGroupsCorrectly() throws InvalidCharacterException {
        Character a = new Warrior("A", 1);
        Character b = new Warrior("B", 1);
        Character c = new Mage("C", 1);
        GuildStatistics stats = new GuildStatistics(List.of(a, b, c), List.of());
        Map<String, Long> distribution = stats.getDistributionByClass();
        assertEquals(2L, distribution.get("Guerrier"));
        assertEquals(1L, distribution.get("Mage"));
    }

    @Test
    void top3RichestIsSortedDescending() throws InvalidCharacterException {
        Character a = new Warrior("A", 1);
        Character b = new Warrior("B", 1);
        Character c = new Warrior("C", 1);
        a.addGold(100);
        b.addGold(50);
        c.addGold(200);
        GuildStatistics stats = new GuildStatistics(List.of(a, b, c), List.of());
        List<Character> top3 = stats.getTop3Richest();
        assertEquals("C", top3.get(0).getName());
        assertEquals("A", top3.get(1).getName());
        assertEquals("B", top3.get(2).getName());
    }

    @Test
    void questSuccessRateIsComputedCorrectly() {
        Quest quest = new Quest("Q", 5, 10, 10);
        QuestResult success = new QuestResult(quest, true, 100);
        QuestResult failure = new QuestResult(quest, false, 10);
        GuildStatistics stats = new GuildStatistics(List.of(), List.of(success, failure));
        assertEquals(0.5, stats.getQuestSuccessRate());
    }

    @Test
    void questSuccessRateIsZeroWhenNoQuests() {
        GuildStatistics stats = new GuildStatistics(List.of(), List.of());
        assertEquals(0.0, stats.getQuestSuccessRate());
    }
}
