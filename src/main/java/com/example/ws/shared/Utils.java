package com.example.ws.shared;

import com.example.ws.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

@Component
public class Utils {
    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static boolean hasTokenExpired(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.getTokenSecret())
                .parseClaimsJws(token)
                .getBody();
        Date expirationDate = claims.getExpiration();
        Date today = new Date();
        return expirationDate.before(today);
    }

    public String generateUserId(int length) {
        return generateRandomString(length);
    }

    public String generateAddressId(int length) {
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder returnValue = new StringBuilder();

        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return returnValue.toString();
    }

    public String generateEmailVerificationToken(String publicUserId) {
        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .setExpiration(new Date(SecurityConstants.EXPIRATION_TIME + System.currentTimeMillis()))
                .setSubject(publicUserId)
                .compact();

        return token;
    }
}
