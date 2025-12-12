package vaultweb.apigateway.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vaultweb.apigateway.exceptions.DefaultException;
import vaultweb.apigateway.exceptions.dto.DefaultExceptionLevels;
import vaultweb.apigateway.model.RefreshToken;
import vaultweb.apigateway.model.User;
import vaultweb.apigateway.repositories.RefreshTokenRepository;

import java.time.Instant;
import java.util.UUID;

/**
 * Service class for handling refresh token operations.
 *
 * @author Calvin Shio
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh.expiration:604800000}") // 7 days
    private Long refreshExpiration;

    /**
     * Creates a new refresh token for the given user.
     *
     * @param user the user for whom the refresh token is created
     * @return the created refresh token
     */
    public Mono<RefreshToken> createRefreshToken(User user) {
        return refreshTokenRepository.deleteByUserId(user.getId())
                .then(Mono.defer(() -> {
                    RefreshToken refreshToken = new RefreshToken();
                    refreshToken.setUserId(user.getId());
                    refreshToken.setToken(UUID.randomUUID().toString());
                    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));
                    refreshToken.setCreatedAt(Instant.now());
                    return refreshTokenRepository.save(refreshToken);
                }));
    }

    /**
     * Verifies if the given refresh token has expired.
     *
     * @param token the refresh token to verify
     * @return the valid refresh token
     * @throws RuntimeException if the token has expired
     */
    public Mono<RefreshToken> verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            return refreshTokenRepository.delete(token)
                    .then(Mono.error(new DefaultException(
                            "Refresh token has expired. Please login again.",
                            DefaultExceptionLevels.AUTHENTICATION_EXCEPTION)));
        }
        return Mono.just(token);
    }
}
