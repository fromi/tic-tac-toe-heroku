package com.github.fromi.tictactoeheroku.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.fromi.tictactoeheroku.security.google.User;

public class RegisteredPlayer implements Player {

    @JsonProperty
    private final User user;

    @JsonProperty
    private boolean ready;

    public RegisteredPlayer(User user) {
        this.user = user;
    }

    public boolean isControlledBy(User user) {
        return this.user.equals(user);
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public void setReady() {
        ready = true;
    }
}
