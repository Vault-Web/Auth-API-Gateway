package vaultweb.apigateway.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import vaultweb.apigateway.model.RefreshToken;

@Repository
public interface RefreshTokenRepository extends ReactiveCrudRepository<RefreshToken, Long> {
    Mono<Void> deleteByUserId(Integer userId);
    Mono<RefreshToken> findByToken(String token);
}
