package com.github.fromi.tictactoeheroku;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GamesController {

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @RequestMapping(method = POST, value = "/game")
    public Game createGame() {
        Game game = new Game();
        simpMessagingTemplate.convertAndSend("/games", game.id);
        return game;
    }

    private class Game {
        public String id = UUID.randomUUID().toString();
    }
}
