package com.example.demo.ts.web;


import com.example.demo.ts.service.TsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TsController {

    @Autowired
    private TsService tsService;

//    @Autowired
//   // @Qualifier("springSecurityFilterChain")
//    private FilterChainProxy filterChainProxy;

    @GetMapping("/chk")
    public String apiPermitAll() {

//        filterChainProxy.getFilterChains().forEach(System.out::println);
//        securityFilterChain.getFilters().forEach(System.out::println);
       return "chk - ok";
    }

    @GetMapping("/manage")
    public String apiHasAuth() {
        return "manage - ok";
    }

    @GetMapping("/suss")
    public String loginSuss() {
        return "suss - ok";
    }

    @GetMapping("/test")
    public String test() throws Exception {
        return tsService.selectUser("amigo").toString();
    }

}
