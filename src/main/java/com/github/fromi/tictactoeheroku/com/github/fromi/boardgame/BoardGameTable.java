package com.github.fromi.tictactoeheroku.com.github.fromi.boardgame;

import static com.github.fromi.tictactoeheroku.com.github.fromi.boardgame.BoardGameTable.State.PLAY;
import static com.github.fromi.tictactoeheroku.com.github.fromi.boardgame.BoardGameTable.State.SETUP;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.fromi.boardgametools.BoardGame;
import com.github.fromi.boardgametools.event.Dispatcher;
import com.github.fromi.boardgametools.event.Event;
import com.github.fromi.tictactoeheroku.game.PlayerNotFoundException;
import com.github.fromi.tictactoeheroku.security.google.User;

public abstract class BoardGameTable<GAME extends BoardGame<PLAYER, ID>, PLAYER, ID extends Serializable> extends Dispatcher {
    @Id
    @JsonProperty
    private String id;

    @JsonProperty
    private State state = SETUP;

    public enum State {
        SETUP, PLAY, OVER
    }

    @JsonIgnore
    protected final Map<String, Player> players = new HashMap<>();

    protected BoardGameTable() {
    }

    protected BoardGameTable(String id, Map<String, Player> players, GAME game) {
        this.id = id;
        setGame(game);
        players.forEach(this::put);
    }

    public void join(User user) {
        Player player = new Player(user);
        put(user.id, player);
        dispatch(new PlayerJoined(player));
    }

    private void put(String userId, Player player) {
        players.put(userId, player);
        propagate(player);
        player.addObserver((Consumer<PlayerChanged>) playerChanged -> {
            if (state == SETUP && players.values().stream().allMatch(Player::isReady)) {
                start();
            }
        });
    }

    public Player getPlayer(User user) {
        if (!players.containsKey(user.id)) {
            throw new PlayerNotFoundException();
        }
        return players.get(user.id);
    }

    protected void start() {
        state = PLAY;
        assignPlayerIds();
        setGame(createGame());
        dispatch(new GameStarted(game));
    }

    protected abstract void assignPlayerIds();

    protected abstract GAME createGame();

    @JsonProperty("players")
    private Map<? extends Serializable, ?> getPlayers() {
        if (state == SETUP) {
            return players;
        } else {
            return players.values().stream().collect(Collectors.toMap(player -> player.id, player -> player));
        }
    }

    @JsonUnwrapped
    @JsonProperty
    private GAME game;

    protected GAME getGame() {
        return game;
    }

    private void setGame(GAME game) {
        this.game = game;
        if (game != null) {
            propagate(game);
            players.values().forEach(player -> player.player = game.getPlayers().get(convertPlayerId(player.id)));
        }
    }

    protected abstract ID convertPlayerId(Serializable id);

    public class Player extends Dispatcher {
        @JsonProperty
        private final User user;

        private boolean ready;

        @JsonProperty
        private Serializable id;

        @Transient
        private PLAYER player;

        public Player(User user) {
            this.user = user;
        }

        @PersistenceConstructor
        private Player(User user, Boolean ready, Serializable id) {
            this.user = user;
            this.ready = ready;
            this.id = id;
        }

        public void setReady() {
            if (!ready) {
                ready = true;
                dispatch(new PlayerChanged(this));
            }
        }

        @JsonProperty
        public boolean isReady() {
            return ready;
        }

        public void setId(ID id) {
            if (player == null) {
                this.id = id;
                dispatch(new PlayerChanged(this));
            }
        }

        @JsonProperty
        @JsonUnwrapped
        public PLAYER getPlayer() {
            return player;
        }
    }

    @Event("player-joined")
    public class PlayerJoined {
        @JsonUnwrapped
        public final Player player;

        public PlayerJoined(Player player) {
            this.player = player;
        }
    }

    @Event("player-changed")
    public class PlayerChanged {
        @JsonUnwrapped
        public final Player player;

        public PlayerChanged(Player player) {
            this.player = player;
        }
    }

    @Event("game-started")
    public class GameStarted {
        @JsonUnwrapped
        public final GAME game;

        public GameStarted(GAME game) {
            this.game = game;
        }
    }
}
