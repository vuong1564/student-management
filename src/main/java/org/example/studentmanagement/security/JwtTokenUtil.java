package org.example.studentmanagement.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {
    private final String jwtSecret = "bb268bb377480e0b1a17c8196deea336a9fb6569b8535ebaed55f54c0dcb69cdf70ab04302034a5c6c8615b726c2fb729d8037970a4a8d8197c903d5674c1e1ef0c301f424df5729f22a3782eacd449249b3f153c4c78fc2f6bb933f2ba4669b3b9b705c9d011b601dac98f68cced8571b0e3c1ada24dbf38cafe0f238ce70f9fd386b76adf8a4c3aa694617a4eb16443cf2e600080f125089c452725ac3d92a8b9e9775fd17d7e4c1741e228673c3b4bab89f9050af709dbbad552a83e5495a6d85dce3e3bd0ab421fa20e7c51a0a2e1a8d86a8b08f17ea30b624f845c77886cc55faea50a0e2bec64422322ba56b6c6cb72a92edc79c2443d0d6cc2aa47558";
    private final long jwtExpirationMs = 1800000; // 1 ngày

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRoleFromToken(String token) {
        return (String) Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }

    public boolean isTokenExpired(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    // Tạo token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    // Kiểm tra token hợp lệ
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("Invalid JWT: " + e.getMessage());
        }
        return false;
    }
}