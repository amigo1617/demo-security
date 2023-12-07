package com.example.demo.ts.handler;

import org.springframework.security.web.RedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ConcurrentSessionExpiredRedirectStrategy implements RedirectStrategy {
    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {

    }
}
