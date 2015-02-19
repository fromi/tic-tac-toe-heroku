package com.github.fromi.tictactoeheroku.game;

import static com.github.fromi.tictactoeheroku.security.URLPathMapping.GAME;
import static com.github.fromi.tictactoeheroku.security.WebSocketDestinationsMapping.GAMES;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.annotation.Resource;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GamesController {

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @RequestMapping(method = POST, value = GAME)
    public Game createGame() {
        Game game = new Game();
        simpMessagingTemplate.convertAndSend(GAMES, game.id);
        return game;
    }

}
