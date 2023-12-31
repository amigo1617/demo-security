package com.example.demo.ts.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (accessDeniedException.getCause() instanceof AuthenticationException) {
            System.out.println(" $$$$$$$$$  CustomAccessDeniedHandler  -  no authentications ");
            response.sendRedirect("/login");
        } else {
            System.out.println(" $$$$$$$$$  CustomAccessDeniedHandler  -  Access Denied ");
            response.sendRedirect("/forbidden");
        }
    }
}
