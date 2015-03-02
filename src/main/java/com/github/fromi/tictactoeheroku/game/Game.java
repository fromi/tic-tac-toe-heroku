package com.github.fromi.tictactoeheroku.game;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.fromi.tictactoeheroku.security.google.User;

public class Game {
    private static final int NUMBER_OF_PLAYERS = 2;

    @Id
    @JsonProperty
    private final String id;

    @JsonProperty
    private final Set<RegisteredPlayer> registeredPlayers;

    @PersistenceConstructor
    private Game(String id, Set<RegisteredPlayer> registeredPlayers) {
        this.id = id;
        this.registeredPlayers = registeredPlayers;
    }

    public Game(User creator) {
        this(null, new HashSet<>(NUMBER_OF_PLAYERS));
        join(creator);
    }

    public boolean join(User user) {
        if (registeredPlayers.size() >= NUMBER_OF_PLAYERS) {
            throw new GameFullException();
        }
        return registeredPlayers.add(new RegisteredPlayer(user));
    }

    public RegisteredPlayer getPlayerControlledBy(User user) {
        return registeredPlayers.stream().filter(player -> player.isControlledBy(user)).findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
