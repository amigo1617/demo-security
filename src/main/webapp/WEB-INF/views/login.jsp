<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="ko">
<head>

</head>
<body>

    <form action="/loginProc" method="post">
        <fieldset>
            <legend>Please Login</legend>
            <!-- use param.error assuming FormLoginConfigurer#failureUrl contains the query parameter error -->
            <c:if test="${param.error != null}">
                <div>
                    Failed to login.
                    <c:if test="${SPRING_SECURITY_LAST_EXCEPTION != null}">
                        Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
                    </c:if>
                </div>
            </c:if>
            <!-- the configured LogoutConfigurer#logoutSuccessUrl is /login?logout and contains the query param logout -->
            <c:if test="${param.logout != null}">
                <div>
                    You have been logged out.
                </div>
            </c:if>
            <p>
                <label for="username">Username</label>
                <input type="text" id="username" name="username"/>
            </p>
            <p>
                <label for="password">Password</label>
                <input type="password" id="password" name="password"/>
            </p>
            <!-- if using RememberMeConfigurer make sure remember-me matches RememberMeConfigurer#rememberMeParameter -->
            <p>
                <label for="remember-me">Remember Me?</label>
                <input type="checkbox" id="remember-me" name="remember-me"/>
            </p>
            <div>
                <button type="submit" class="btn">Log in</button>
            </div>
        </fieldset>
    </form>
</body>
</html>