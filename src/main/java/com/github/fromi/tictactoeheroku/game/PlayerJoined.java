package com.github.fromi.tictactoeheroku.game;

import com.github.fromi.tictactoeheroku.security.google.User;

public class PlayerJoined {

    public final User user;

    public PlayerJoined(User user) {
        this.user = user;
    }
}
