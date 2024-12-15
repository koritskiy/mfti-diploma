package com.koritski.teamsync.gateway.util;

import com.koritski.teamsync.gateway.exception.JwtTokenMalformedException;
import com.koritski.teamsync.gateway.exception.JwtTokenMissingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;

    public Claims getClaims(final String token) {
        try {
            log.info(Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().toString());
            return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.info(e.getMessage() + " => " + e);
        }
        return null;
    }

    public void validateToken(final String token) throws JwtTokenMalformedException, JwtTokenMissingException {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        } catch (MalformedJwtException ex) {
            throw new JwtTokenMalformedException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new JwtTokenMalformedException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new JwtTokenMalformedException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new JwtTokenMissingException("JWT claims string is empty.");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
