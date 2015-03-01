package com.github.fromi.tictactoeheroku.game;

import com.github.fromi.tictactoeheroku.google.User;
import com.github.fromi.tictactoeheroku.security.WebSocketDestinationsMapping;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.github.fromi.tictactoeheroku.security.URLPathMapping.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class GamesController {

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Resource
    private GameRepository repository;

    @RequestMapping(method = POST, value = GAME)
    public Game createGame(@AuthenticationPrincipal User user) {
        Game game = repository.save(new Game(user));
        simpMessagingTemplate.convertAndSend(WebSocketDestinationsMapping.GAMES, game);
        return game;
    }

    @RequestMapping(method = GET, value = GAMES)
    public List<Game> getGames() {
        return repository.findAll();
    }

    @RequestMapping(method = GET, value = GAME + "/{id}")
    public Game getGame(@PathVariable String id) {
        return repository.findOne(id);
    }

    @MessageMapping("/game/{id}/join")
    public void joinGame(@DestinationVariable String id, @AuthenticationPrincipal User user) {
        Game game = repository.findOne(id);
        if (game.join(user)) {
            simpMessagingTemplate.convertAndSend(WebSocketDestinationsMapping.GAME + "/" + id + "/joined", user);
            repository.save(game);
        }
    }

    @MessageMapping("/game/{id}/ready")
    public void startGame(@DestinationVariable String id, @AuthenticationPrincipal User user) {
        Game game = repository.findOne(id);
        game.ready(user);
        repository.save(game);
    }
}
