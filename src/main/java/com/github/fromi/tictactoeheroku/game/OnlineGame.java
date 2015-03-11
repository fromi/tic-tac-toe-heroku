package com.github.fromi.tictactoeheroku.game;

import static com.github.fromi.tictactoeheroku.game.OnlineGame.State.PLAY;
import static com.github.fromi.tictactoeheroku.game.OnlineGame.State.SETUP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.fromi.boardgametools.event.Dispatcher;
import com.github.fromi.tictactoe.Player;
import com.github.fromi.tictactoe.TicTacToe;
import com.github.fromi.tictactoe.material.Mark;
import com.github.fromi.tictactoeheroku.security.google.User;

@Document(collection = "game")
public class OnlineGame extends Dispatcher {
    private static final int NUMBER_OF_PLAYERS = 2;

    @Id
    @JsonProperty
    private String id;

    @JsonProperty
    private State state = SETUP;

    @JsonProperty
    private final Map<String, PlayingUser> playingUsers;

    @JsonUnwrapped
    @JsonProperty
    private TicTacToe game;

    public OnlineGame(User creator) {
        playingUsers = new HashMap<>();
        join(creator);
    }

    @PersistenceConstructor
    private OnlineGame(String id, Map<String, PlayingUser> playingUsers, TicTacToe game) {
        this.id = id;
        this.playingUsers = playingUsers;
        this.game = game;
        playingUsers.values().forEach(this::observe);
        propagate(game);
    }

    public void join(User user) {
        if (playingUsers.size() >= NUMBER_OF_PLAYERS) {
            throw new GameFullException();
        }
        PlayingUser playingUser = new PlayingUser(user);
        playingUsers.put(user.id, playingUser);
        observe(playingUser);
        dispatch(new PlayerJoined(user));
    }

    private void observe(PlayingUser playingUser) {
        propagate(playingUser);
        playingUser.observe((Consumer<PlayerStatusChanged>) playerStatusChanged -> playerStatusChangedHandler());
    }

    private void playerStatusChangedHandler() {
        if (playingUsers.values().stream().allMatch(PlayingUser::ready)) {
            start();
        }
    }

    private void start() {
        game = new TicTacToe();
        List<PlayingUser> users = new ArrayList<>(playingUsers.values());
        Collections.shuffle(users);
        users.get(0).playsAs(Mark.X);
        users.get(1).playsAs(Mark.O);
        state = PLAY;
        propagate(game);
        dispatch(new GameStarted(this));
    }

    public PlayingUser getPlayingUser(User user) {
        if (!playingUsers.containsKey(user.id)) {
            throw new PlayerNotFoundException();
        }
        return playingUsers.get(user.id);
    }

    public Player getPlayerControlledBy(User user) {
        return game.player(getPlayingUser(user).playsAs);
    }

    public enum State {
        SETUP, PLAY, OVER
    }
}
