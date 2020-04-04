package cn.wuxiangknow.single.common.jwt;






import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT TOKEN MANAGER
 */
@Component
public class JwtTokenManager implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    private static final String CLAIM_KEY_USER_ID = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.expiration}")
    private Long expiration;
    //密钥
    private byte[] mac = new byte[]{-128, 38, 81, -49, 67, -29, 21, 14, -83, -76, -9, -5, 36, 65, 110, -62, 11, -109, 15, -40, 112, -91, -50, -92, 48, 84, 72, -88, 19, 51, 40, -120, 74, -24, 75, 5, 24, -73, 65, 49, -43, 13, -66, -30, -72, 83, -19, 88, 87, -92, 4, 97, 70, 127, 38, -81, -46, 14, -87, -69, 119, -73, -96, -124};

    private SecretKey key = new SecretKeySpec(mac, SignatureAlgorithm.HS512.getJcaName());


    public String getUserIdFromToken(String token) {
        String userId;
        try {
            Claims claims = getClaimsFromToken(token);
            userId = (String) claims.get(CLAIM_KEY_USER_ID);
        } catch (Exception e) {
            userId = null;
        }
        return userId;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate(Long expiration) {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(String token ) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }
    private Boolean isCreatedBeforeLastLogin(Date created, Date lastLogin) {
        return (lastLogin != null && created.before(lastLogin));
    }
    public String generateToken(String id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USER_ID, id);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims,expiration);
    }

    private String generateToken(Map<String, Object> claims ,Long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate(expiration))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token  , Date lastPasswordReset) {
        Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && !isTokenExpired(token);
    }

    public String refreshToken(String token ) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims  ,expiration);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token,String userId,Date lastLoginDate ,Date lastPasswordRestDate) {
        String id = getUserIdFromToken(token);
        Date created = getCreatedDateFromToken(token);
        return (
                id.equals(userId)
                        && !isTokenExpired(token)
                        && !isCreatedBeforeLastPasswordReset(created, lastPasswordRestDate)
                        && !isCreatedBeforeLastLogin(created, lastLoginDate));
    }
}
