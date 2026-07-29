package com.guildmaster.guild;

import com.guildmaster.exception.InvalidTeamException;
import com.guildmaster.model.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Une équipe de 3 à 5 personnages envoyée en quête. */
public class Team {
    public static final int MIN_SIZE = 3;
    public static final int MAX_SIZE = 5;

    private final String id;
    private final List<Character> members;

    public Team(List<Character> members) throws InvalidTeamException {
        if (members == null || members.size() < MIN_SIZE || members.size() > MAX_SIZE) {
            throw new InvalidTeamException(
                    "Une équipe doit contenir entre " + MIN_SIZE + " et " + MAX_SIZE + " membres");
        }
        if (members.stream().distinct().count() != members.size()) {
            throw new InvalidTeamException("Une équipe ne peut pas contenir de doublons");
        }
        this.id = UUID.randomUUID().toString();
        this.members = new ArrayList<>(members);
    }

    /** Puissance totale de l'équipe = somme des puissances des membres. */
    public int getTotalPower() {
        return members.stream().mapToInt(Character::getPower).sum();
    }

    public List<Character> getMembers() {
        return new ArrayList<>(members);
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Équipe (" + members.size() + " membres, puissance=" + getTotalPower() + ")";
    }
}
