package com.eticaret.stajflo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Base64 kodlu dizeyi alacağız
    @Value("${jwt.secret}")
    private String secretString;

    @Value("${jwt.expiration}")
    private Long expiration;

    // SecretKey döndürmek daha modern bir yaklaşımdır.
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretString);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // HATA 2 İÇİN EKLENEN METOT: 'extractExpiration' metodu eksikti.
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // HATA 1 İÇİN DÜZELTİLEN METOT: Yeni jjwt API'sine göre ayrıştırma.
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // Deprecated olan setSigningKey yerine verifyWith kullanılır.
                .build()
                .parseSignedClaims(token) // Deprecated olan parseClaimsJws yerine parseSignedClaims kullanılır.
                .getPayload(); // getBody() yerine getPayload() kullanılır.
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // İsterseniz buraya ekstra roller, yetkiler vb. ekleyebilirsiniz.
        // claims.put("roles", userDetails.getAuthorities());
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                // İYİLEŞTİRME: signWith(Key, Algorithm) metodu deprecated oldu.
                // Sadece anahtar vermek yeterlidir, algoritma anahtarın türünden anlaşılır.
                .signWith(getSigningKey())
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}