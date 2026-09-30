package com.example.jwtnimbus.service;

import com.example.jwtnimbus.exception.JwtValidationException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtExpiration);

        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .subject(userDetails.getUsername())
                .issueTime(now)
                .expirationTime(expiration);

        extraClaims.forEach(claimsBuilder::claim);

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.HS256)
                        .type(JOSEObjectType.JWT)
                        .build(),
                claimsBuilder.build()
        );

        try {
            signedJWT.sign(new MACSigner(getSecretBytes()));
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new JwtValidationException("Không thể ký JWT", e);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        JWTClaimsSet claims = extractAllClaims(token);
        String username = claims.getSubject();
        Date expiration = claims.getExpirationTime();

        return username != null
                && username.equals(userDetails.getUsername())
                && expiration != null
                && expiration.after(new Date());
    }

    private JWTClaimsSet extractAllClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            if (!JWSAlgorithm.HS256.equals(signedJWT.getHeader().getAlgorithm())) {
                throw new JwtValidationException("Thuật toán JWT không hợp lệ");
            }

            boolean verified = signedJWT.verify(new MACVerifier(getSecretBytes()));
            if (!verified) {
                throw new JwtValidationException("Chữ ký JWT không hợp lệ");
            }

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            Date expiration = claims.getExpirationTime();
            if (expiration == null || expiration.before(new Date())) {
                throw new JwtValidationException("JWT đã hết hạn");
            }

            return claims;
        } catch (ParseException | JOSEException | IllegalArgumentException e) {
            if (e instanceof JwtValidationException jwtEx) {
                throw jwtEx;
            }
            throw new JwtValidationException("JWT không hợp lệ", e);
        }
    }

    private byte[] getSecretBytes() {
        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(secretKey);
        } catch (IllegalArgumentException ex) {
            // Cho phép dùng raw secret nếu người học nhập trực tiếp chuỗi.
            keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        }

        if (keyBytes.length < 32) {
            throw new JwtValidationException("Secret key HS256 phải có ít nhất 256 bit (32 bytes)");
        }
        return keyBytes;
    }
}
