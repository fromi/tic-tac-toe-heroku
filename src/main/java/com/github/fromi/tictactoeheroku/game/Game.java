package com.github.fromi.tictactoeheroku.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.fromi.tictactoeheroku.google.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public class Game {
    @Id
    public final String id;

    @JsonProperty
    private final Set<User> users;

    @PersistenceConstructor
    private Game(String id, Set<User> users) {
        this.id = id;
        this.users = users;
    }

    public Game(User creator) {
        this(null, newHashSet(creator));
    }
}
