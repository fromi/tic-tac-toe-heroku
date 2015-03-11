package com.github.fromi.tictactoeheroku.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.fromi.boardgametools.event.Dispatcher;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.fromi.tictactoeheroku.game.BoardGame.State.*;

public abstract class BoardGame<GAME, PLAYER> extends Dispatcher {
    @Id
    @JsonProperty
    private String id;

    @JsonProperty
    private State state = SETUP;

    protected final Map<String, PlayingUser> players;

    protected BoardGame() {
        players = new HashMap<>();
    }

    protected BoardGame(String id, Map<String, PlayingUser> players) {
        this.id = id;
        this.players = players;
    }

    @JsonProperty("players")
    private Map<? extends Serializable, ?> players() {
        if (state == SETUP) {
            return players;
        } else {
            Map<Serializable, PlayerWrapper> result;
            result = getPlayers().entrySet().stream().collect(Collectors.toMap(
                    Map.Entry::getKey, entry -> new PlayerWrapper(players.get(entry.getKey()), entry.getValue())));
            return result;
        }
    }

    protected abstract Map<? extends Serializable, PLAYER> getPlayers();

    protected void start() {
        state = PLAY;
    }

    public enum State {
        SETUP, PLAY, OVER
    }

    private class PlayerWrapper {
        @JsonProperty
        @JsonUnwrapped
        private final PlayingUser playingUser;

        @JsonProperty
        @JsonUnwrapped
        private final PLAYER player;

        public PlayerWrapper(PlayingUser playingUser, PLAYER player) {
            this.playingUser = playingUser;
            this.player = player;
        }
    }
}
