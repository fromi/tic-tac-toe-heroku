package com.github.fromi.tictactoeheroku;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ThymeleafConfigurer extends WebMvcConfigurerAdapter {

    private static final String ROOT_PATH = "/";
    private static final String HOME_VIEW = "home";

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(ROOT_PATH).setViewName(HOME_VIEW);
    }
}
