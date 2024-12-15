package com.koritski.teamsync.backend.util;

import com.koritski.teamsync.backend.exception.JwtTokenMalformedException;
import com.koritski.teamsync.backend.exception.JwtTokenMissingException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.koritski.teamsync.backend.constants.AuthConstants.*;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public Claims getClaims(final String token) {
        try {
            return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            System.out.println(e.getMessage() + " => " + e);
        }
        return null;
    }

    public void validateToken(final String token) throws JwtTokenMalformedException, JwtTokenMissingException {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        } catch (SignatureException ex) {
            throw new JwtTokenMalformedException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new JwtTokenMalformedException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new JwtTokenMalformedException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new JwtTokenMalformedException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new JwtTokenMissingException("JWT claims string is empty.");
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get(ID_CLAIMS_KEY, Long.class);
    }

    public String getUserEmailFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get(EMAIL_CLAIMS_KEY, String.class);
    }

    public boolean isUserAdmin(String token) {
        Claims claims = getClaims(token);
        return claims.get(IS_ADMIN_ROLE_EXIST, boolean.class);
    }
}
