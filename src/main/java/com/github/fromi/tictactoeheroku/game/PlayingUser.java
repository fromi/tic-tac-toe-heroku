package com.github.fromi.tictactoeheroku.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.fromi.boardgametools.event.Dispatcher;
import com.github.fromi.tictactoeheroku.security.google.User;

import static com.github.fromi.tictactoeheroku.game.PlayingUser.Status.*;

public class PlayingUser extends Dispatcher {

    @JsonProperty
    @JsonUnwrapped
    private final User user;

    @JsonProperty
    private Status status = REGISTERED;

    private Object playsAs;

    public PlayingUser(User user) {
        this.user = user;
    }

    public void setReady() {
        if (status == REGISTERED) {
            status = READY;
            dispatch(new PlayerStatusChanged(user.id, status));
        }
    }

    public enum Status {
        REGISTERED, READY, PLAYING, GONE
    }
}
