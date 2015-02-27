package com.github.fromi.tictactoeheroku.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;

public class CsrfHeaderFilter extends OncePerRequestFilter {

    private static final String XSRF_TOKEN = "XSRF-TOKEN";
    private static final CookieGenerator COOKIE_GENERATOR = new CookieGenerator();
    static {COOKIE_GENERATOR.setCookieName(XSRF_TOKEN);}

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrf != null) {
            Cookie cookie = WebUtils.getCookie(request, XSRF_TOKEN);
            if (cookie == null || !csrf.getToken().equals(cookie.getValue())) {
                COOKIE_GENERATOR.addCookie(response, csrf.getToken());
            }
        }
        filterChain.doFilter(request, response);
    }
}
