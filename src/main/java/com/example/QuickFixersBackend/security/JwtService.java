package com.example.QuickFixersBackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private static  final String SECRET_KEY="DE7D1440A4930996A6822E1FBEC6C3900B7CE1FA251A15AAB5D73C912312D7E8";

    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return  claimResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return  generateToken(new HashMap<>(),userDetails);
    }

    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails) {

        List<String> authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .claims(extractClaims)
                .claim("authorities", authorities)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24))
                .signWith(getSignInKey())
                .compact();
    }



    public boolean isTokenValid(String token , UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExperation(token).before(new Date());
    }

    private Date extractExperation(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims (String token){
        return Jwts.parser()
                .verifyWith(getSignInKey()) // verify the sender of the jwt is who it claims to be and ensure the message does not change at all.
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
