package com.github.fromi.tictactoeheroku.game;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.fromi.boardgametools.event.Event;

@Event("game-started")
public class GameStarted {

    @JsonUnwrapped
    public final OnlineGame game;

    public GameStarted(OnlineGame game) {
        this.game = game;
    }
}
