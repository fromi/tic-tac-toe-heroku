package com.github.fromi.tictactoeheroku;

import static com.github.fromi.tictactoeheroku.security.google.OpenIDConnectAuthenticationFilter.LOGIN_PATH;
import static com.jayway.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.jayway.restassured.RestAssured;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class SecurityIT {

    @Value("${local.server.port}")
    private int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void welcomePageNotRedirected() {
        given().redirects().follow(false).when().get("/").then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void loginRedirectToOauthProvider() {
        given().redirects().follow(false).when().get(LOGIN_PATH).then().statusCode(HttpStatus.SC_MOVED_TEMPORARILY);
    }
}
