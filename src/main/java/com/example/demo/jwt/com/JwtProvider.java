package com.example.demo.jwt.com;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.Password;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Date;


public final class JwtProvider {

    private static final String SEED = "ucm-vuc-oper jwt api password seed";

    private static final Password PWD = Keys.password(SEED.toCharArray());


    public static String createJwt(@NonNull String custname) {
        Assert.hasLength(custname, "claim 'custname' is null. ");
        Date now = new Date();
        return Jwts.builder()
                .issuer("ucm-vuc-oper") // UCM
                .issuedAt(now)
                .expiration(new Date(now.getTime() + Duration.ofMinutes(1).toMillis()))
                .claim("custname", custname)
                .subject("balance-api-token")
                .encryptWith(PWD, Jwts.KEY.PBES2_HS512_A256KW, Jwts.ENC.A256GCM)
                .compact();
    }

    public static Claims parseJwt(@NonNull String authorizationHeader) {
        Assert.hasLength(authorizationHeader, "");
        Assert.state(authorizationHeader.startsWith("Bearer "), "");
        String token = authorizationHeader.substring("Bearer ".length());
        return Jwts.parser()
                .decryptWith(PWD)
                .build()
                .parseEncryptedClaims(token)
                .getPayload();
    }
}
