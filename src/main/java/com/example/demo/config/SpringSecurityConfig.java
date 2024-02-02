package com.example.demo.config;

import com.example.demo.ts.handler.*;
import com.example.demo.ts.provider.CustomAuthenticationProvider;
import com.example.demo.ts.service.CustomUserDetailService;
import com.example.demo.ts.voter.UrlAccessDecisionVoter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
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
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/test", "/error", "/forbidden", "/api/**").permitAll() // relate WebExpressionVoter
                .anyRequest().denyAll() // 위 permitall 만 WebExpressionVoter 이용, 나머지는 UrlAccessDecisionVoter 에서 인가 (쿼리해서 등등..)
                .accessDecisionManager(this.accessDecisionManager())

            .and()
                .csrf().disable()
                .formLogin()
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/loginProc")
                .successHandler(customAuthenticationSuccessHandler())
                .failureHandler(customAuthenticationFailureHandler())

            .and()
                .logout()
                .logoutUrl("/logout").permitAll()
                //.addLogoutHandler(new LogoutHandler())  세션초기화 외에 해야할 루틴이 있다면
                .logoutSuccessHandler(customLogoutSuccessHandler())

            .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint())
                .accessDeniedHandler(customAccessDeniedHandler())

            .and()
                .sessionManagement()
                .invalidSessionUrl("/login")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(new SessionRegistryImpl())
                .expiredUrl("/login");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/favicon.ico", "/static/h2-console", "/static/js/**", "/static/css/**");
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean // .sessionRegistry(new SessionRegistryImpl()) 이거에 대응...내장톰캣일시.. 아닌경우 ServletContextInitializer -> onStartup(ServletContext servletContext) - > servletContext.addListener(new HttpSessionEventPublisher()); 이렇게 등록해야함
    public ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }


    @Bean
    public LogoutSuccessHandler customLogoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
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
        return new CustomAuthenticationProvider(customUserDetailsService(), passwordEncoder());
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
