package com.github.fromi.tictactoeheroku.game;

import static com.github.fromi.tictactoeheroku.game.PlayingUser.Status.READY;
import static com.github.fromi.tictactoeheroku.game.PlayingUser.Status.REGISTERED;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.fromi.boardgametools.event.Dispatcher;
import com.github.fromi.tictactoe.material.Mark;
import com.github.fromi.tictactoeheroku.security.google.User;

public class PlayingUser extends Dispatcher {

    @JsonProperty
    @JsonUnwrapped
    private final User user;

    @JsonProperty
    private Status status = REGISTERED;

    Mark playsAs;

    public PlayingUser(User user) {
        this.user = user;
    }

    public void setReady() {
        if (status == REGISTERED) {
            status = READY;
            dispatch(new PlayerStatusChanged(user.id, status));
        }
    }

    public boolean ready() {
        return status == READY;
    }

    void playsAs(Mark playerId) {
        playsAs = playerId;
    }

    public enum Status {
        REGISTERED, READY, PLAYING, GONE
    }
}
