package san.projectdates.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import san.projectdates.core.entities.User;

public class JwtService {
  private final SecretKey key;

  public JwtService(){
    Dotenv dotenv = Dotenv.load();
    String secret = dotenv.get("JWT_SECRETKEY");
  
    if(secret == null | secret.length() < 32){
      throw new RuntimeException("No se ha cargado bien el SecretKey");
    }

    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String createToken(User user){
    return Jwts.builder()
      .subject(user.getEmail())
      .claim("userId", user.getId())
      .claim("role", user.getRole())
      .signWith(key)
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + 3600000))
      .compact();
  }

  public Claims parseToken(String token){
    try{
      return Jwts.parser()
        .verifyWith(this.key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
    }catch(JwtException | IllegalArgumentException e){
      throw new RuntimeException("Token invalido o expirado: " + e.getMessage());
    }
  }
}
