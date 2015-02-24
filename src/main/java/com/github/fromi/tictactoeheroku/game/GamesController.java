package com.github.fromi.tictactoeheroku.game;

import static com.github.fromi.tictactoeheroku.security.URLPathMapping.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.fromi.tictactoeheroku.security.WebSocketDestinationsMapping;

@RestController
public class GamesController {

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Resource
    private GameRepository repository;

    @RequestMapping(method = POST, value = GAME)
    public Game createGame() {
        Game game = repository.save(new Game());
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
}
