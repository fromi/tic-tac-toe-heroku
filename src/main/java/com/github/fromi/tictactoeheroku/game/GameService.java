package com.github.fromi.tictactoeheroku.game;

import static com.github.fromi.tictactoeheroku.security.websocket.WebSocketDestinationsMapping.SLASH_JOINER;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.github.fromi.boardgametools.event.Event;
import com.github.fromi.tictactoeheroku.com.github.fromi.boardgame.BoardGameTable;
import com.github.fromi.tictactoeheroku.security.google.User;
import com.github.fromi.tictactoeheroku.security.websocket.WebSocketDestinationsMapping;

@Service
public class GameService {

    @Resource
    private GameRepository repository;

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    public TicTacToe createGame(User user) {
        TicTacToe game = repository.save(new TicTacToe(user));
        simpMessagingTemplate.convertAndSend(WebSocketDestinationsMapping.GAMES, game);
        return game;
    }

    public List<TicTacToe> getGames() {
        return repository.findAll();
    }

    @Cacheable("games")
    public TicTacToe playGame(String id) {
        TicTacToe game = repository.findOne(id);
        game.addObserver((Consumer<BoardGameTable.GameStarted>) gameStarted -> repository.save(game));
        game.addObserver((Consumer<Object>) event -> dispatch(id, event));
        return game;
    }

    private void dispatch(String gameId, Object event) {
        Event eventAnnotation = event.getClass().getAnnotation(Event.class);
        String eventName = eventAnnotation != null ? eventAnnotation.value() : event.getClass().getSimpleName();
        simpMessagingTemplate.convertAndSend(SLASH_JOINER.join(WebSocketDestinationsMapping.GAME, gameId, eventName), event);
    }
}
