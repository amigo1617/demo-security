package com.example.demo.ts.voter;

import com.example.demo.ts.service.TsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;
import java.util.Map;

public class UrlAccessDecisionVoter implements AccessDecisionVoter<FilterInvocation> {

    @Autowired
    private TsService tsService;

    private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();


    @Override
    public int vote(Authentication authentication, FilterInvocation filterInvocation, Collection<ConfigAttribute> attributes) {
        // 로그인 없이 secure url 접근의 경우 바로 ACCESS_DENIED
        if (authenticationTrustResolver.isAnonymous(authentication)) {
            System.out.println(String.format("UrlAccessDecisionVoter : anonymous - user    uri : %s ", filterInvocation.getRequestUrl()));
            System.out.println("need login ");
            return ACCESS_DENIED;
        }

        Map<String,String> user = null;
        try {
            user = tsService.selectUser(authentication.getName());
        } catch (Exception e) {

        }
        // for access test
        if ("/manage".equals(filterInvocation.getRequestUrl())) {
            System.out.println(String.format("UrlAccessDecisionVoter :  prohibited!!   uri : %s ", filterInvocation.getRequestUrl()));
            return ACCESS_DENIED;
        }
        System.out.println(String.format("UrlAccessDecisionVoter ACCESS_GRANTED : %s : uri : %s ", user.get("USERNAME"), filterInvocation.getRequestUrl()));
        return ACCESS_GRANTED;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }
}
