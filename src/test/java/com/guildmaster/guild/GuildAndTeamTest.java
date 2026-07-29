package com.guildmaster.guild;

import com.guildmaster.exception.CharacterNotFoundException;
import com.guildmaster.exception.InvalidCharacterException;
import com.guildmaster.exception.InvalidTeamException;
import com.guildmaster.model.Character;
import com.guildmaster.model.Mage;
import com.guildmaster.model.Warrior;
import com.guildmaster.quest.Quest;
import com.guildmaster.quest.QuestResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuildAndTeamTest {

    @Test
    void teamWithTooFewMembersThrows() throws InvalidCharacterException {
        Character a = new Warrior("A", 1);
        Character b = new Mage("B", 1);
        assertThrows(InvalidTeamException.class, () -> new Team(List.of(a, b)));
    }

    @Test
    void teamWithDuplicateMemberThrows() throws InvalidCharacterException {
        Character a = new Warrior("A", 1);
        Character b = new Mage("B", 1);
        Character c = new Warrior("C", 1);
        assertThrows(InvalidTeamException.class, () -> new Team(List.of(a, b, a, c)));
    }

    @Test
    void validTeamHasCorrectPower() throws InvalidCharacterException, InvalidTeamException {
        Character a = new Warrior("A", 1);
        Character b = new Mage("B", 1);
        Character c = new Warrior("C", 1);
        Team team = new Team(List.of(a, b, c));
        assertEquals(a.getPower() + b.getPower() + c.getPower(), team.getTotalPower());
    }

    @Test
    void recruitingAddsCharacterToGuild() throws InvalidCharacterException {
        Guild guild = new Guild();
        Character a = new Warrior("A", 1);
        guild.recruit(a);
        assertEquals(1, guild.getAllCharacters().size());
        assertFalse(guild.getHistory().isEmpty());
    }

    @Test
    void gettingUnknownCharacterThrows() {
        Guild guild = new Guild();
        assertThrows(CharacterNotFoundException.class, () -> guild.getCharacter("inconnu"));
    }

    @Test
    void resolvingQuestRecordsResult() throws InvalidCharacterException, InvalidTeamException {
        Guild guild = new Guild();
        // Équipe volontairement très puissante pour garantir un succès quasi certain
        Character a = new Warrior("A", 20);
        Character b = new Warrior("B", 20);
        Character c = new Warrior("C", 20);
        Team team = new Team(List.of(a, b, c));
        Quest quest = new Quest("Quête facile", 1, 30, 60);

        QuestResult result = guild.resolveQuest(quest, team);

        assertEquals(1, guild.getQuestResults().size());
        assertEquals(quest, result.getQuest());
    }
}
