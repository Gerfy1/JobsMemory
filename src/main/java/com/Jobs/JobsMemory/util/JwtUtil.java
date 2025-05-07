package com.Jobs.JobsMemory.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public JwtUtil() {
    }

    public String extractUsername(String token) {
        return (String)this.extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return (Date)this.extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = this.extractAllClaims(token);
        return (T)claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            logger.error("Erro ao fazer parse ou validar assinatura do token: {}", e.getMessage());
            throw e;
        }
    }


    private Boolean isTokenExpired(String token) {
        try {
            return this.extractExpiration(token).before(new Date());
        } catch (Exception e) {
            logger.error("Erro ao verificar token expirado: {}", e.getMessage());
            return true;
        }

    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap();
        return this.createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + 36000000L;
        Date exp = new Date(expMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, this.secret)
                .compact();
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        String usernameFromToken = null;
        String usernameFromUserDetails = null;
        boolean isUsernameMatch = false;
        boolean isNotExpired = false;

        try {
            usernameFromToken = this.extractUsername(token);

            if (userDetails != null) {
                usernameFromUserDetails = userDetails.getUsername();
                isUsernameMatch = usernameFromToken.equals(usernameFromUserDetails);
            } else {
                logger.warn("UserDetails fornecido para isTokenValid é nulo!");
                isUsernameMatch = false;
            }

            isNotExpired = !this.isTokenExpired(token);

            logger.info("--- Iniciando Validação do Token ---");
            logger.info("Username extraído do Token : '{}'", usernameFromToken);
            logger.info("Username vindo do UserDetails: '{}'", usernameFromUserDetails);
            logger.info("Resultado da comparação (equals): {}", isUsernameMatch);
            logger.info("Resultado da verificação (!isTokenExpired): {}", isNotExpired);
            logger.info("Resultado final de isTokenValid: {}", (isUsernameMatch && isNotExpired));
            logger.info("--- Fim da Validação do Token ---");

        } catch (Exception e) {
            logger.error("Erro inesperado durante isTokenValid: {}", e.getMessage(), e);
            return false;
        }

        return isUsernameMatch && isNotExpired;
    }
}
