package com.github.fromi.tictactoeheroku.game;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.annotation.Resource;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GamesController {

    public static final String GAME_PATH = "/game";
    public static final String GAMES_PATH = "/games";

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @RequestMapping(method = POST, value = GAME_PATH)
    public Game createGame() {
        Game game = new Game();
        simpMessagingTemplate.convertAndSend(GAMES_PATH, game.id);
        return game;
    }

}
