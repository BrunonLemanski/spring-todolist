package pl.retrilx.todolist.security;

import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.retrilx.todolist.domain.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static pl.retrilx.todolist.security.SecurityConstants.EXPIRATION_TIME;
import static pl.retrilx.todolist.security.SecurityConstants.SECRET_KEY;

@Component
public class JwtTokenProvider {

    //Generate token

    public String generateToken(Authentication authentication){
        User user = (User)authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis()); //getting system date

        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME); //variable which used to set user session | the expiration time is added to current time
                                                                       //expiryDate it is the time in which session ends

        String userId = Long.toString(user.getId());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", (Long.toString(user.getId())));
        claims.put("username", user.getUsername());
        claims.put("fullName", user.getFullname());

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    //Validate token
    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        }catch (SignatureException e){
            System.out.println("Invalid JWT Signature");
        }catch (MalformedJwtException e){
            System.out.println("Invalid JWT Token");
        }catch (ExpiredJwtException e){
            System.out.println("Expired JWT Token");
        }catch (UnsupportedJwtException e){
            System.out.println("Unsupported JWT Token");
        }catch (IllegalArgumentException e){
            System.out.println("JWT claims string is empty");
        }
        return false;
    }

    //Get user id from token
    public Long getUserIdFromJwt(String token){

        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        String id = (String)claims.get("id");

        return Long.parseLong(id);
    }
}
