package vaultweb.apigateway.service.auth;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import vaultweb.apigateway.exceptions.DefaultException;
import vaultweb.apigateway.exceptions.dto.DefaultExceptionLevels;
import vaultweb.apigateway.model.RefreshToken;
import vaultweb.apigateway.model.User;
import vaultweb.apigateway.repositories.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

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

  private static String generateRefreshToken() {
    SecureRandom secureRandom = new SecureRandom();
    Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();
    byte[] randomBytes = new byte[32];
    secureRandom.nextBytes(randomBytes);
    return base64Encoder.encodeToString(randomBytes);
  }

  /**
   * Creates a new refresh token for the given user.
   *
   * @param user the user for whom the refresh token is created
   * @return the created refresh token
   */
  public Mono<RefreshToken> createRefreshToken(User user) {
    return refreshTokenRepository
        .deleteByUserId(user.getId())
        .then(
            Mono.defer(
                () -> {
                  RefreshToken refreshToken = new RefreshToken();
                  refreshToken.setUserId(user.getId());
                  refreshToken.setToken(generateRefreshToken());
                  refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));
                  refreshToken.setCreatedAt(Instant.now());
                  return refreshTokenRepository.save(refreshToken);
                }));
  }

  /**
   * Verifies if the given refresh token has expired.
   *
   * @param token the refresh token to verify
   * @return a {@link Mono} that emits the valid refresh token if it has not expired,
   *     or completes with a {@link DefaultException} error signal if the token has expired
   */
  public Mono<RefreshToken> verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      return refreshTokenRepository
          .delete(token)
          .then(
              Mono.error(
                  new DefaultException(
                      "Refresh token has expired. Please login again.",
                      DefaultExceptionLevels.AUTHENTICATION_EXCEPTION)));
    }
    return Mono.just(token);
  }
}
