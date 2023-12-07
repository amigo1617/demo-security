package com.example.demo.config;

import com.example.demo.ts.handler.*;
import com.example.demo.ts.provider.CustomAuthenticationProvider;
import com.example.demo.ts.service.CustomUserDetailService;
import com.example.demo.ts.voter.UrlAccessDecisionVoter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.Duration;
import java.util.List;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                    .authorizeRequests()
                    .antMatchers("/test", "/error", "/api/**").permitAll() // relate WebExpressionVoter
                    .anyRequest().denyAll() // due to AnonymousAuthenticationToken
                    .accessDecisionManager(this.accessDecisionManager())
            .and()
                    .formLogin()
                    .successHandler(new CustomAuthenticationSuccessHandler())
                    .failureHandler(new CustomAuthenticationFailureHandler())
            .and()
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler())
                //.accessDeniedPage()
//                .authenticationEntryPoint(customAuthenticationEntryPoint())
//            .and()
//                    .sessionManagement()
//                    .invalidSessionStrategy(new CustomInvalidSessionStrategy(""))
//                    .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::migrateSession)

            .and()
                    .csrf().disable();
                    //.httpBasic(); // http header - Authorization: Basic bzFbdGfmZrptWY30YQ==  필요하다면 세팅
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favicon.ico", "/static/h2-console", "/static/js/**", "/static/css/**");
        //web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(customUserDetailsService());
    }

    @Bean
    public UserDetailsService customUserDetailsService() {
        return new CustomUserDetailService();
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        return new AffirmativeBased(List.of(new WebExpressionVoter(), this.accessDecisionVoter()));
    }

    @Bean
    public AccessDecisionVoter<FilterInvocation> accessDecisionVoter() {
        return new UrlAccessDecisionVoter();
    }

    //@Bean
    // BCryptPasswordEncoder는 Spring Security에서 제공하는 비밀번호 암호화 객체입니다.
    // Service에서 비밀번호를 암호화할 수 있도록 Bean으로 등록합니다.
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    // AuthenticationManager 까지 컨피그 해야될시
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.authenticationProvider(null);
//        return authenticationManagerBuilder.build();
//    }


//    @Bean
//    @Profile({"develop", "default"})
//    @ConditionalOnProperty(name = "sys", havingValue = "dev")
//    public String print() {
//        System.out.println("****************** ConditionalOnProperty - loaded ");
//        return "";
//    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(10))
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .build();
    }
}
