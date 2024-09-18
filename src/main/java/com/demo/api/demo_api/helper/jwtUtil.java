package com.demo.api.demo_api.helper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

import org.springframework.stereotype.Component;

import com.demo.api.demo_api.helper.exception.CustomException;

@Component
public class jwtUtil {

  // Gunakan SecretKey dalam bentuk byte array
  private final String secretKey = "ca1106c7-62d9-447c-8822-26244da33219";
  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  // Metode untuk menghasilkan JWT
  public String generateToken(UUID userId) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", userId);
    // return createToken(claims, userId.toString()); // Jika ingin menambahkan
    // subject(Opsional)
    return createToken(claims);
  }

  // Jika ingin menambahkan subject(Opsional)
  // Buat token dengan klaim dan subject (userId)
  // private String createToken(Map<String, Object> claims, String subject) { //
  private String createToken(Map<String, Object> claims) {
    // Konversi secretKey menjadi Key untuk signing
    Key key = getSigningKey();

    return Jwts.builder()
        .setClaims(claims) // Optional claims
        // .setSubject(subject) // Jika ingin userId menjadi subject bukan id
        .setIssuedAt(new Date(System.currentTimeMillis())) // Waktu penerbitan
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expiry dalam 10 jam
        // .setExpiration(new Date(System.currentTimeMillis() + 1000)) // Expiry dalam 1 detik
        .signWith(key, signatureAlgorithm) // Signing JWT dengan key dan algoritma HS256
        .compact();
  }

  private Key getSigningKey() {
    byte[] keyBytes = secretKey.getBytes(); // Ubah secret key menjadi byte array
    return new SecretKeySpec(keyBytes, signatureAlgorithm.getJcaName()); // Buat Key untuk signing
  }

  public UUID parseUserId(String token) {
    try {
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(secretKey.getBytes())
          .build()
          .parseClaimsJws(token)
          .getBody();
      if (claims.getExpiration().before(new Date())) {
        throw new CustomException("Token expired");
      }
      return UUID.fromString(claims.get("userId").toString());
    } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
      throw new CustomException("Invalid token");
    }
  }

}
