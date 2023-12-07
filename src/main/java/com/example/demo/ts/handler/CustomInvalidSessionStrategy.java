package com.example.demo.ts.handler;

import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomInvalidSessionStrategy implements InvalidSessionStrategy {

    private String expiredUrl;

    public CustomInvalidSessionStrategy(String expiredUrl){
        this.expiredUrl = expiredUrl;
    }

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Ajax 호출인 경우
        if (request.getHeader("X-AjaxRequest") != null && "1".equals(request.getHeader("X-AjaxRequest"))) {
            response.sendError(601, "This session has been expired");
        } else {
            String contextPath = request.getContextPath();
            String loginUrl = expiredUrl;

            response.setContentType("text/html;charset=UTF-8");

            PrintWriter out = response.getWriter();
            out.println("<script type=\"text/javascript\">\n");
            out.println("if(window.opener){\n");
            out.println("    window.self.close();\n");
            out.println("    window.opener.location.href=\"" + contextPath + loginUrl + "\";\n");
            out.println("}else{\n");
            out.println("    window.top.location.href=\"" + contextPath + loginUrl + "\";\n");
            out.println("}\n");
            out.println("</script>\n");
            out.flush();
            out.close();

        }
    }
}
