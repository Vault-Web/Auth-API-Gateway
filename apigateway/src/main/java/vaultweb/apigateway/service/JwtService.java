package vaultweb.apigateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private JwtParser parser = Jwts.parser()
        .verifyWith(getSigningKey())
        .build();

    private static final long EXPIRATION_TIME_MILLIS = 3600000; // 1 hour

    /**
     * Creates a secret key to sign JWT tokens.
     * <p>
     * It always creates the exact same key, with the same secret key.
     * This method decodes the secret key from the application.properties file into a BASE 64 encoded string.
     * It is done to ensure that the secret is properly formed, since {@code SECRET_KEY} could contain non UTF-8 characters.
     * @return {@code SecretKey}
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Checks if the token is valid.
     * <p>
     * It verifies the token's signature and expiration time.
     * @param token the token to be validated
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    public boolean isValid(final String token) {
        return isUnmodified(token) && !isExpired(token);
    }

    /**
     * Generates a JWT token.
     * <p>
     * The generated token is valid for {@code EXPIRATION_TIME_MILLIS} milliseconds.
     * Additionally, the token is always signed with the {@code SECRET_KEY} to ensure its integrity.
     * The token is always going to contain the user's id and name.
     * @param id of the user
     * @param username of the user
     * @return the generated signed jwt token as a {@link String}
     */
    public String generateToken(final Integer id, final String username) {
        return Jwts.builder()
            .subject(username)
            .claim("userId", id)
            .issuedAt(new Date())
            .expiration(
                new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS)
            )
            .signWith(getSigningKey())
            .compact();
    }

    /**
     * Extracts all claims from the given token.
     * @param token the token to extract claims from
     * @return the extracted claims as a {@link Claims} object
     */
    private Claims extractAllClaims(final String token) {
        return this.parser.parseSignedClaims(token).getPayload();
    }

    /**
     * Extracts the expiration date of the token.
     * @param token the token to extract claims from
     * @return the expiration date of the token and return it as a {@link Date}
     */
    private Date extractExpiration(final String token) {
        return extractAllClaims(token).getExpiration();
    }

    /**
     * Checks if the token is expired.
     * @param token the token to check
     * @return true if the token is expired and false otherwise
     */
    private boolean isExpired(final String token) {
        return this.extractExpiration(token).before(new Date());
    }

    /**
     * Checks if the token is unmodified.
     * @param token the token to check
     * @return true if the token is unmodified and false otherwise
     */
    private boolean isUnmodified(final String token) {
        try {
            this.parser.parseSignedClaims(token);
            return true;
        } catch (final JwtException e) {
            return false;
        }
    }
}
