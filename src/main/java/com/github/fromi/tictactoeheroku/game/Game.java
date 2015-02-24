package com.github.fromi.tictactoeheroku.game;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Game {
    @JsonProperty
    @Id
    public String id;
}
