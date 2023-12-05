package com.example.demo.jwt.web;


import com.example.demo.jwt.com.JwtProvider;
import com.example.demo.jwt.service.TokenApiService;
import com.example.demo.jwt.vo.BalanceResultErrorVo;
import com.example.demo.jwt.vo.BalanceResultVo;
import com.example.demo.jwt.vo.CustTokenVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class JwtController {

    @PostMapping("/token")
    public String getToken(@RequestBody CustTokenVO custTokenVO) {
        // username validation need
        return JwtProvider.createJwt(custTokenVO.getCustname());
    }

    @GetMapping("/balance")
    public BalanceResultVo getBalance(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        Claims claim = JwtProvider.parseJwt(authorizationHeader);
        String custname = (String) claim.get("custname");
        // select balance using custname
        BalanceResultVo balanceResultVo = new BalanceResultVo();
        balanceResultVo.setBalance("100,000");
        balanceResultVo.setResult("succeed");
        return balanceResultVo;
    }

    @ExceptionHandler
    public BalanceResultErrorVo malformedJwtExceptions(Exception e) {
        return new BalanceResultErrorVo(e.getMessage());
    }
}
