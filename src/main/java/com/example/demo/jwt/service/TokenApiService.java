package com.example.demo.jwt.service;

import com.example.demo.jwt.vo.BalanceResultVo;
import com.example.demo.jwt.vo.TokenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenApiService {

    @Autowired
    private RestTemplate restTemplate;

    private String token_url = "http://localhost:8080/api/token";

    private String balance_url = "http://localhost:8080/api/balance";


    public String getBalance(@NonNull String custname) {
        Assert.hasLength(custname, "'custname' is null. processing abort..");
        String token = this.getToken(custname);
        String balance;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<BalanceResultVo> responseEntity = restTemplate.exchange(balance_url, HttpMethod.GET, requestEntity, BalanceResultVo.class);
        BalanceResultVo balanceResultVo = responseEntity.getBody();
        if ("failed".equals(balanceResultVo.getResult())) {
            if (balanceResultVo.getReason().startsWith("JWT expired")) {
                TokenContext.remove(custname);
                balance = this.getBalance(custname); // recursive
            } else {
                throw new IllegalStateException(String.format(" api call failed. reason is - %s", balanceResultVo.getReason()));
            }
        } else {
            balance = balanceResultVo.getBalance();
        }

        return balance;
    }

    private String getToken(@NonNull String custname) {
        String token = TokenContext.get(custname);
        if (Objects.isNull(token)) {

//            ResponseEntity<TokenVO> responseEntity = restTemplate.postForEntity(token_url, Map.of("custname", custname), TokenVO.class);
//            TokenVO tokenVO = responseEntity.getBody();
//            if ("failed".equals(tokenVO.getResult())) {
//                throw new IllegalStateException(String.format(" token api call failed. reason is - %s", tokenVO.getReason()));
//            }
//            token = tokenVO.getToken();
            token = restTemplate.postForObject(token_url, Map.of("custname", custname), String.class);
            TokenContext.set(custname, token);
        }
        return token;
    }


    private static class TokenContext {

        static final Map<String, String> TOKEN_MAP = new ConcurrentHashMap<>();

        static String get(String custname) {
            return TOKEN_MAP.get(custname);
        }

        static void set(String custname, String token) {
            TOKEN_MAP.put(custname, token);
        }

        static void remove(String custname) {
            TOKEN_MAP.remove(custname);
        }
    }
}
