package com.github.fromi.tictactoeheroku.security;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.github.fromi.tictactoeheroku.security.google.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @RequestMapping(method = GET, value = URLPathMapping.USER)
    public User getUser(@AuthenticationPrincipal User user) {
        return user;
    }
}
