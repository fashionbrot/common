package com.github.fashionbrot.util;

import io.jsonwebtoken.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

public class JwtUtil {


    // 生成JWT Token
    public static String generateToken(Map<String, Object> claims,
                                        int expiresInSecond,
                                        SignatureAlgorithm signatureAlgorithm,
                                        String secretKey) {
        return Jwts.builder()
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiresInSecond * 1000))
                .signWith(signatureAlgorithm, secretKey)
                .compact();
    }





    // 解析JWT Token（不验证）
    public static Claims decodeToken(String token,PublicKey publicKey)
            throws ExpiredJwtException,
            UnsupportedJwtException,
            MalformedJwtException,
            SignatureException,
            IllegalArgumentException {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
    }


    public static Claims decodeToken(String secretKey,String token)
            throws ExpiredJwtException,
            UnsupportedJwtException,
            MalformedJwtException,
            SignatureException,
            IllegalArgumentException {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }




    public static String generateToken(Map<String, Object> claims,
                                        int expiresInSecond,
                                        SignatureAlgorithm signatureAlgorithm,
                                        PrivateKey privateKey) {
        return Jwts.builder()
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiresInSecond * 1000))
                .signWith(signatureAlgorithm, privateKey)
                .compact();
    }



}
