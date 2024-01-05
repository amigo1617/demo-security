package com.example.demo.ts.web;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TsController {

    @PostMapping(value = "test", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String test(@RequestParam String param, String value) throws Exception {
         return param;
    }
}
