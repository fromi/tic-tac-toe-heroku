package com.github.fromi.tictactoeheroku.game;

import static com.github.fromi.tictactoeheroku.security.websocket.WebSocketDestinationsMapping.SLASH_JOINER;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.github.fromi.boardgametools.event.Event;
import com.github.fromi.tictactoeheroku.security.google.User;
import com.github.fromi.tictactoeheroku.security.websocket.WebSocketDestinationsMapping;

@Service
public class GameService {

    @Resource
    private GameRepository repository;

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    public OnlineGame createGame(User user) {
        OnlineGame game = repository.save(new OnlineGame(user));
        simpMessagingTemplate.convertAndSend(WebSocketDestinationsMapping.GAMES, game);
        return game;
    }

    public List<OnlineGame> getGames() {
        return repository.findAll();
    }

    public OnlineGame getGame(String id) {
        return repository.findOne(id);
    }

    @Cacheable("games")
    public OnlineGame playGame(String id) {
        OnlineGame game = repository.findOne(id);
        game.observe((Consumer<Object>) event -> dispatch(id, event));
        return game;
    }

    private void dispatch(String gameId, Object event) {
        Event eventAnnotation = event.getClass().getAnnotation(Event.class);
        String eventName = eventAnnotation != null ? eventAnnotation.value() : event.getClass().getSimpleName();
        simpMessagingTemplate.convertAndSend(SLASH_JOINER.join(WebSocketDestinationsMapping.GAME, gameId, eventName), event);
    }
}
