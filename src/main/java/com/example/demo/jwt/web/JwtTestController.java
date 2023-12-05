package com.example.demo.jwt.web;

import com.example.demo.jwt.service.TokenApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("api/test")
public class JwtTestController {

    @Autowired
    private TokenApiService tokenApiService;


    @GetMapping("/balance")
    public String getBalance() {
        return tokenApiService.getBalance("amigo");
    }

}
