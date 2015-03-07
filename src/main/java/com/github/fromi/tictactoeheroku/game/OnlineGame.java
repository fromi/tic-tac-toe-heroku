package com.github.fromi.tictactoeheroku.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.fromi.boardgametools.event.Dispatcher;
import com.github.fromi.tictactoe.TicTacToe;
import com.github.fromi.tictactoeheroku.security.google.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "game")
public class OnlineGame extends Dispatcher {
    private static final int NUMBER_OF_PLAYERS = 2;

    @Id
    @JsonProperty
    private String id;

    @JsonProperty
    private final Map<String, PlayingUser> playingUsers;

    @JsonUnwrapped
    private TicTacToe game;

    @PersistenceConstructor
    private OnlineGame(String id, Map<String, PlayingUser> playingUsers, TicTacToe game) {
        this.id = id;
        this.playingUsers = playingUsers;
        this.game = game;
        propagate(playingUsers.values());
    }

    public OnlineGame(User creator) {
        playingUsers = new HashMap<>();
        join(creator);
    }

    public void join(User user) {
        if (playingUsers.size() >= NUMBER_OF_PLAYERS) {
            throw new GameFullException();
        }
        PlayingUser playingUser = new PlayingUser(user);
        playingUsers.put(user.id, playingUser);
        propagate(playingUser);
        dispatch(new PlayerJoined(user));
    }

    public PlayingUser getPlayerControlledBy(User user) {
        if (!playingUsers.containsKey(user.id)) {
            throw new PlayerNotFoundException();
        }
        return playingUsers.get(user.id);
    }
}
