package com.hepsiemlak.todo.security.jwt;

import com.hepsiemlak.todo.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTokenProvider {

    @Value("${hepsiemlak.todo.app.jwtSecret}")
    private String APP_SECRET;

    @Value("${hepsiemlak.todo.app.jwtExpirationMs}")
    private int EXPIRES_IN;

    public String generateJTWToken(Authentication auth){

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        Date expirateDate = new Date(System.currentTimeMillis() + EXPIRES_IN);

        return Jwts.builder()
                .setSubject(userDetails.getId())
                .setIssuedAt(new Date())
                .setExpiration(expirateDate)
                .signWith(SignatureAlgorithm.HS512,APP_SECRET)
                .compact();
    }

    String getUserIdFromJWT(String token){
        Claims claims =Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    private boolean isTokenExpired(String token){
        Date expiration = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }

    boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
            return !isTokenExpired(token);
        }catch(SignatureException e){
            return false;
        }catch(MalformedJwtException e){
            return false;
        }catch(ExpiredJwtException e){
            return false;
        }catch(UnsupportedJwtException e){
            return false;
        }catch(IllegalArgumentException e){
            return false;
        }

    }
}
