package com.github.fromi.tictactoeheroku.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import com.github.fromi.tictactoe.Game;
import com.github.fromi.tictactoe.Player;
import com.github.fromi.tictactoe.material.Mark;
import com.github.fromi.tictactoeheroku.com.github.fromi.boardgame.BoardGameTable;
import com.github.fromi.tictactoeheroku.security.google.User;

@Document(collection = "game")
public class TicTacToe extends BoardGameTable<Game, Player, Mark> {
    private static final int NUMBER_OF_PLAYERS = 2;

    public TicTacToe(User creator) {
        join(creator);
    }

    @PersistenceConstructor
    private TicTacToe(String id, Map<String, Player> players, Game game) {
        super(id, players, game);
    }

    @Override
    protected Mark convertPlayerId(Serializable id) {
        if (id instanceof Mark) {
            return (Mark) id;
        } else if (id instanceof String) {
            return Mark.valueOf((String) id);
        } else {
            throw new IllegalArgumentException("Cannot create Mark from " + id);
        }
    }

    @Override
    public void join(User user) {
        if (players.size() >= NUMBER_OF_PLAYERS) {
            throw new GameFullException();
        }
        super.join(user);
    }

    @Override
    protected void assignPlayerIds() {
        List<Player> players = new ArrayList<>(this.players.values());
        Collections.shuffle(players);
        players.get(0).setId(Mark.X);
        players.get(1).setId(Mark.O);
    }

    @Override
    protected Game createGame() {
        return new Game();
    }
}
