package com.github.fromi.tictactoeheroku.game;

import static com.github.fromi.tictactoeheroku.security.URLPathMapping.GAME;
import static com.github.fromi.tictactoeheroku.security.URLPathMapping.GAMES;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.fromi.tictactoeheroku.security.google.User;

@RestController
public class GamesController {

    @Resource
    private GameService service;

    @RequestMapping(method = POST, value = GAME)
    public OnlineGame createGame(@AuthenticationPrincipal User user) {
        return service.createGame(user);
    }

    @RequestMapping(method = GET, value = GAMES)
    public List<OnlineGame> getGames() {
        return service.getGames();
    }

    @RequestMapping(method = GET, value = GAME + "/{id:[A-Za-z0-9]*}")
    public OnlineGame getGame(@PathVariable String id) {
        return service.playGame(id);
    }

    @MessageMapping("/game/{id}/join")
    public void joinGame(@DestinationVariable String id, @AuthenticationPrincipal User user) {
        service.playGame(id).join(user);
    }

    @MessageMapping("/game/{id}/ready")
    public void playerReady(@DestinationVariable String id, @AuthenticationPrincipal User user) {
        service.playGame(id).getPlayerControlledBy(user).setReady();
    }
}
