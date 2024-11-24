package com.ng.bsa.service;


import com.ng.bsa.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${spring.secretKey}")
    private String secretKey;
    private final UserRepository userRepository;

    public String generateJwtToken(Map<String, Object> extraClaims,
                                   String email) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 20000))
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key generateKey() {
        byte[] bytes = Decoders.BASE64URL.decode(secretKey);
        System.out.println("bytes is " + bytes.toString());
        return Keys.hmacShaKeyFor(bytes);
    }


    private Claims extractAllClaims(String jwt) {
        return Jwts.parser().setSigningKey(generateKey()).build()
                .parseSignedClaims(jwt).getBody();
    }

    private <T> T extractSpecificClaim(Claims claims,
                                       Function<Claims,T> claimsTFunction){
        return claimsTFunction.apply(claims);
    }

    public String extractEmail(String jwt){
        Claims claims = extractAllClaims(jwt);
        return extractSpecificClaim(claims,Claims::getSubject);
    }

    public Date extractExpiration(String jwt){
        Claims claims = extractAllClaims(jwt);
        return extractSpecificClaim(claims,Claims::getExpiration);
    }

    public boolean isEmailValid(String email){
        return userRepository.existsByEmail(email);
    }

    public boolean isTokenValid(String jwt){
        Date expiry = extractExpiration(jwt);
        return expiry.after(new Date());
    }

}
