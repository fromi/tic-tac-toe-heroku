package com.github.fromi.tictactoeheroku;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    @RequestMapping("/game")
    public String createGame() {
        return "All good, identified!";
    }
}
