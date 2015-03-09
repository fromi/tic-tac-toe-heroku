package com.github.fromi.tictactoeheroku.game;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.fromi.tictactoeheroku.security.google.User;

public class PlayerJoined {

    @JsonUnwrapped
    public final User user;

    public PlayerJoined(User user) {
        this.user = user;
    }
}
