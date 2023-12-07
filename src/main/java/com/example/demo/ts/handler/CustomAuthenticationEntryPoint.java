package com.example.demo.ts.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    AccessDeniedHandler accessDeniedHandler;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            // 한계정으로 1, 2 번 두개의창 이용시 1번창에서 로그아웃(세션클리어) 후 재로그인없이 2번창에서 요청을 보낸경우(세션이 없는경우)
            accessDeniedHandler.handle(request, response, null);
        } else {
            // 권한없음 혹은 잘못된 URL 입력시
            accessDeniedHandler.handle(request, response, new AccessDeniedException("CustomAuthenticationEntryPoint : AuthenticationException", authException));
        }
    }

}
