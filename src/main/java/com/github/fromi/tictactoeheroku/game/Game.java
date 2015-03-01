package com.github.fromi.tictactoeheroku.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.fromi.tictactoeheroku.google.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.HashSet;
import java.util.Set;

public class Game {
    public static final int NUMBER_OF_PLAYERS = 2;
    @Id
    public final String id;

    @JsonProperty
    private final Set<RegisteredPlayer> registeredPlayers;

    @PersistenceConstructor
    private Game(String id, Set<RegisteredPlayer> registeredPlayers) {
        this.id = id;
        this.registeredPlayers = registeredPlayers;
    }

    public Game(User creator) {
        this(null, new HashSet<>());
        join(creator);
    }

    public boolean join(User user) {
        if (registeredPlayers.size() >= NUMBER_OF_PLAYERS) {
            throw new IllegalStateException();
        }
        return registeredPlayers.add(new RegisteredPlayer(user));
    }

    public void ready(User user) {
        if (registeredPlayers.size() == NUMBER_OF_PLAYERS) {
            getRegisteredPlayer(user).ready = true;
        }
    }

    private RegisteredPlayer getRegisteredPlayer(User user) {
        return registeredPlayers.stream().filter(player -> player.user.equals(user)).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public class RegisteredPlayer {

        @JsonProperty
        private final User user;

        @JsonProperty
        private boolean ready;

        public RegisteredPlayer(User user) {
            this.user = user;
        }
    }
}
