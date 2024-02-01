package com.example.demo.ts.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final Logger log = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    // call when .. AccessDecisionVoter 에서 ACCESS_DENIED 시  authentication 이 없으면 AuthenticationEntryPoint 로 진행 , 아니면 here
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.debug(" $$$$$$$$$  CustomAccessDeniedHandler  -  Access Denied ");
        response.sendRedirect("/forbidden");
    }
}
