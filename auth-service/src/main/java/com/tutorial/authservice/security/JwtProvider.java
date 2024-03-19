package com.tutorial.authservice.security;

import com.tutorial.authservice.entity.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    @Value("${jwt.private-key-location}")
    private String privateKeyLocation;

    @Value("${jwt.public-key-location}")
    private String publicKeyLocation;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    @PostConstruct
    public void init() {
        try {

            ClassPathResource publicResource = new ClassPathResource(publicKeyLocation);
            InputStream publicStream = publicResource.getInputStream();
            byte[] publicKeyBytes = publicStream.readAllBytes();

            // Decodificar la clave pública desde formato PEM
            String publicKeyPEM = new String(publicKeyBytes);
            String publicKeyPEMTrimmed = publicKeyPEM
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replace("\n", "");
            byte[] decodedPublicKeyBytes = Base64.getDecoder().decode(publicKeyPEMTrimmed);

            // Convertir la clave pública a un objeto PublicKey
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decodedPublicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(publicKeySpec);

            // Cargar la clave privada
            ClassPathResource privateResource = new ClassPathResource(privateKeyLocation);
            InputStream privateStream = privateResource.getInputStream();
            byte[] privateKeyBytes = privateStream.readAllBytes();

            // Decodificar la clave privada desde formato PEM
            String privateKeyPEM = new String(privateKeyBytes);
            String privateKeyPEMTrimmed = privateKeyPEM
                    .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                    .replace("-----END RSA PRIVATE KEY-----", "")
                    .replace("\n", "");
            byte[] decodedPrivateKeyBytes = Base64.getDecoder().decode(privateKeyPEMTrimmed);

            // Convertir la clave privada a un objeto PrivateKey
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decodedPrivateKeyBytes);
            privateKey = keyFactory.generatePrivate(privateKeySpec);
        } catch (Exception e) {

        }
    }

    public String createToken(AuthUser authUser) {
        // Definimos la fecha de emisión (now) y la fecha de expiración (exp)
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + 3600000; // 1 hora de expiración
        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);

        // Construimos el token JWT
        return Jwts.builder()
                .subject(authUser.getUserName()) // Agregamos el sujeto al token
                .claim("id", authUser.getId()) // Agregamos otros claims según sea necesario
                .issuedAt(now)
                .expiration(exp)
                .signWith(privateKey)
                .compact();
    }

    public boolean validate(String token){
        try{
            Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token); //validamos que la clave sea la correcta
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String getUserNameFromToken(String token){
        try{
            return Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token).getPayload().getSubject();
        }catch (Exception e){
            return "bad token";
        }
    }
}
