package com.projectsassy.sassy.token;

import com.projectsassy.sassy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String key = "38rhewf8rfi39rh9ijr2398rfj9f38rjf9w23r8jf89ef3r89r389fj932r8j89we3rj892";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

        } catch (Exception e) {
            log.error("만료된 토큰");
        }
    }
}
