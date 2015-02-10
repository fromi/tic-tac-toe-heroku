package com.github.fromi.tictactoeheroku.google;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings({"WeakerAccess", "UnusedDeclaration"}) // Used by Thymeleaf
public class UserInfo {
    public final String id;
    public final String name;

    @JsonCreator
    public UserInfo(@JsonProperty("id") String id, @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }
}
