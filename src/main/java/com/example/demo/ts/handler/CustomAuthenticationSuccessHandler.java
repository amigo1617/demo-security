package com.example.demo.ts.handler;

import com.example.demo.ts.vo.CustomUserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("#################  CustomAuthentificationSuccessHandler success :" + authentication.isAuthenticated());
        System.out.println(((CustomUserDetail)authentication.getPrincipal()).getUsername());
        response.sendRedirect("/chk");

      /*        로그인시 로그인 전 보던 화면으로 이동이 필요할시

        SavedRequest savedRequest = requestCache.getRequest(request,response);
        if(savedRequest != null){
            // 인증 받기 전 url로 이동하기
            redirectStrategy.sendRedirect(request, response, savedRequest.getRedirectUrl());
        }else{
            // 기본 url로 가도록
            redirectStrategy.sendRedirect(request, response, "/");
        }*/
        
    }
}
