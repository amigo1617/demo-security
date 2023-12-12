package com.example.demo.ts.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// call when .. 모든 voter가 ACCESS_DENIED 던지고 로그인을 하지 않아 AuthenticationToken 이 AnonymousAuthenticationToken 인 경우
//(Authentication -> AnonymousAuthenticationToken [Principal=anonymousUser, Credentials=[PROTECTED], Authenticated=true, Details=..., Granted Authorities=[ROLE_ANONYMOUS]])
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    AccessDeniedHandler accessDeniedHandler;


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // caused by 미인증(익명인증 포함) secure url 접근시
        System.out.println(" &&&&&&&  CustomAuthenticationEntryPoint  -  no authentications   need login" );
        response.sendRedirect("/login");
    }

}
