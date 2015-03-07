package com.github.fromi.tictactoeheroku.game;

import static com.github.fromi.tictactoeheroku.security.URLPathMapping.GAME;
import static com.github.fromi.tictactoeheroku.security.URLPathMapping.GAMES;
import static com.github.fromi.tictactoeheroku.security.websocket.WebSocketDestinationsMapping.SLASH_JOINER;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Resource;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.fromi.tictactoeheroku.security.google.User;
import com.github.fromi.tictactoeheroku.security.websocket.WebSocketDestinationsMapping;

@RestController
public class GamesController {

    private static final String PLAYER_JOINED = "player-joined";
    private static final String PLAYER_STATUS_CHANGED = "player-status-changed";

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @Resource
    private GameRepository repository;

    @RequestMapping(method = POST, value = GAME)
    public OnlineGame createGame(@AuthenticationPrincipal User user) {
        OnlineGame game = repository.save(new OnlineGame(user));
        simpMessagingTemplate.convertAndSend(WebSocketDestinationsMapping.GAMES, game);
        return game;
    }

    @RequestMapping(method = GET, value = GAMES)
    public List<OnlineGame> getGames() {
        return repository.findAll();
    }

    @RequestMapping(method = GET, value = GAME + "/{id}")
    public OnlineGame getGame(@PathVariable String id) {
        return repository.findOne(id);
    }

    @MessageMapping("/game/{id}/join")
    public void joinGame(@DestinationVariable String id, @AuthenticationPrincipal User user) {
        OnlineGame game = repository.findOne(id);
        game.observe((Consumer<PlayerJoined>) playerJoined -> {
            repository.save(game);
            simpMessagingTemplate.convertAndSend(SLASH_JOINER.join(WebSocketDestinationsMapping.GAME, id, PLAYER_JOINED), user);
        });
        game.join(user);
    }

    @MessageMapping("/game/{id}/ready")
    public void playerReady(@DestinationVariable String id, @AuthenticationPrincipal User user) {
        OnlineGame game = repository.findOne(id);
        game.observe((Consumer<PlayerStatusChanged>) playerStatusChanged -> {
            repository.save(game);
            simpMessagingTemplate.convertAndSend(SLASH_JOINER.join(WebSocketDestinationsMapping.GAME, id, PLAYER_STATUS_CHANGED), playerStatusChanged);
        });
        PlayingUser playingUser = game.getPlayerControlledBy(user);
        playingUser.setReady();
    }
}
