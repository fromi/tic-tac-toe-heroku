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
    private State state;

    @PersistenceConstructor
    private Game(String id, State state) {
        this.id = id;
        this.state = state;
    }

    public Game(User creator) {
        this(null, new Setup());
        join(creator);
    }

    public boolean join(User user) {
        return state.join(user);
    }

    public Player getPlayerControlledBy(User user) {
        return state.getPlayerControlledBy(user);
    }

    private interface State {
        boolean join(User user);

        Player getPlayerControlledBy(User user);
    }

    private static class Setup implements State {
        @JsonProperty
        private final Set<RegisteredPlayer> players;

        private Setup() {
            players = new HashSet<>();
        }

        @PersistenceConstructor
        private Setup(Set<RegisteredPlayer> players) {
            this.players = players;
        }

        @Override
        public boolean join(User user) {
            if (players.size() >= NUMBER_OF_PLAYERS) {
                throw new GameFullException();
            }
            return players.add(new RegisteredPlayer(user));
        }

        @Override
        public Player getPlayerControlledBy(User user) {
            return players.stream().filter(player -> player.isControlledBy(user)).findFirst().orElseThrow(IllegalArgumentException::new);
        }
    }
}
