package com.github.fromi.tictactoeheroku.game;

public class PlayerStatusChanged {

    public final String playerId;
    public final PlayingUser.Status status;

    public PlayerStatusChanged(String playerId, PlayingUser.Status status) {
        this.playerId = playerId;
        this.status = status;
    }
}
