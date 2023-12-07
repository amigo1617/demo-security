package com.example.demo.ts.web;


import com.example.demo.ts.service.TsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.Filter;
import java.util.List;

@RestController
public class TsController {

    @Autowired
    private TsService tsService;

    @Autowired
    @Qualifier("springSecurityFilterChain")
    private Filter springSecurityFilterChain;


    @GetMapping("/chk")
    public String apiChk() {
        System.out.println("*********************************************************");
        FilterChainProxy filterChainProxy = (FilterChainProxy) springSecurityFilterChain;
        List<SecurityFilterChain> list = filterChainProxy.getFilterChains();
        list.stream()
                .flatMap(chain -> chain.getFilters().stream())
                .forEach(filter -> System.out.println(filter.getClass()));
        System.out.println("*********************************************************");
       return "chk - ok";
    }

    @GetMapping("/manage")
    public String apiHasAuth() {
        return "manage - ok";
    }

    @GetMapping("/error")
    public String error(String msg) {
        return msg;
    }

    @GetMapping("/test")
    public String test() throws Exception {
        return tsService.selectUser("amigo").toString();
    }

}
