package com.github.fromi.tictactoeheroku.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.fromi.tictactoe.Player;
import com.github.fromi.tictactoe.TicTacToe;
import com.github.fromi.tictactoe.material.Mark;
import com.github.fromi.tictactoeheroku.security.google.User;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Document(collection = "game")
public class OnlineGame extends BoardGame<TicTacToe, Player> {
    private static final int NUMBER_OF_PLAYERS = 2;

    public OnlineGame(User creator) {
        join(creator);
    }

    @PersistenceConstructor
    private OnlineGame(String id, Map<String, PlayingUser> players, TicTacToe game) {
        super(id, players);
        this.game = game;
        players.values().forEach(this::observe);
        propagate(game);
    }

    @Override
    protected Map<? extends Serializable, Player> getPlayers() {
        return game.players;
    }

    @JsonUnwrapped
    @JsonProperty
    private TicTacToe game;

    public void join(User user) {
        if (players.size() >= NUMBER_OF_PLAYERS) {
            throw new GameFullException();
        }
        PlayingUser playingUser = new PlayingUser(user);
        players.put(user.id, playingUser);
        observe(playingUser);
        dispatch(new PlayerJoined(user));
    }

    private void observe(PlayingUser playingUser) {
        propagate(playingUser);
        playingUser.observe((Consumer<PlayerStatusChanged>) playerStatusChanged -> playerStatusChangedHandler());
    }

    private void playerStatusChangedHandler() {
        if (players.values().stream().allMatch(PlayingUser::ready)) {
            start();
        }
    }

    protected void start() {
        game = new TicTacToe();
        List<PlayingUser> users = new ArrayList<>(players.values());
        Collections.shuffle(users);
        users.get(0).playsAs(Mark.X);
        users.get(1).playsAs(Mark.O);
        super.start();
        propagate(game);
        dispatch(new GameStarted(this));
    }

    public PlayingUser getPlayingUser(User user) {
        if (!players.containsKey(user.id)) {
            throw new PlayerNotFoundException();
        }
        return players.get(user.id);
    }

    public Player getPlayerControlledBy(User user) {
        return game.player(getPlayingUser(user).playsAs);
    }
}
