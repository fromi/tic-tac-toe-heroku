package com.github.fromi.tictactoeheroku.google;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty
    public final String name;

    @JsonProperty
    public final String id;

    @JsonCreator
    public User(@JsonProperty("id") String id, @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }
}