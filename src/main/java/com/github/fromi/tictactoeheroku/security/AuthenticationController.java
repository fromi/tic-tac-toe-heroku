package com.github.fromi.tictactoeheroku.security;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.fromi.tictactoeheroku.google.UserInfo;

@RestController
public class AuthenticationController {

    public static final String USER_PATH = "/user";

    @RequestMapping(method = GET, value = USER_PATH)
    public UserInfo getUserInfo(@AuthenticationPrincipal UserInfo userInfo) {
        return userInfo;
    }
}
