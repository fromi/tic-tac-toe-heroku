package com.github.fromi.tictactoeheroku.game;

import static com.github.fromi.tictactoeheroku.security.websocket.WebSocketDestinationsMapping.SLASH_JOINER;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.github.fromi.tictactoeheroku.security.google.User;
import com.github.fromi.tictactoeheroku.security.websocket.WebSocketDestinationsMapping;

@Service
public class GameService {
    private static final String PLAYER_JOINED = "player-joined";
    private static final String PLAYER_STATUS_CHANGED = "player-status-changed";

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
        game.observe((Consumer<PlayerJoined>) playerJoined -> {
            repository.save(game);
            simpMessagingTemplate.convertAndSend(SLASH_JOINER.join(WebSocketDestinationsMapping.GAME, id, PLAYER_JOINED), playerJoined);
        });
        game.observe((Consumer<PlayerStatusChanged>) playerStatusChanged -> {
            repository.save(game);
            simpMessagingTemplate.convertAndSend(SLASH_JOINER.join(WebSocketDestinationsMapping.GAME, id, PLAYER_STATUS_CHANGED), playerStatusChanged);
        });
        return game;
    }
}
