package com.github.fromi.tictactoeheroku.security;

import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.fromi.tictactoeheroku.google.UserInfo;

@RestController
public class AuthenticationController {
    @RequestMapping("/user")
    public UserInfo getUserInfo(@AuthenticationPrincipal UserInfo userInfo) {
        return userInfo;
    }
}
