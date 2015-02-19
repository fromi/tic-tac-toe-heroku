package com.github.fromi.tictactoeheroku.game;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Game {
    @JsonProperty
    public final String id = UUID.randomUUID().toString();
}
