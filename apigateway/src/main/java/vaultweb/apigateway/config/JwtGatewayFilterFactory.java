package vaultweb.apigateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Configuration;
import vaultweb.apigateway.util.JwtUtil;

@Configuration
public class JwtGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    private final JwtUtil jwtUtil;

    public JwtGatewayFilterFactory(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String username = jwtUtil.extractSubject(
                    exchange.getRequest().getHeaders().getFirst("Authorization").substring(7)
            );

            return chain.filter(exchange.mutate()
                    .request(exchange.getRequest().mutate()
                            .header("X-Auth-Username", username)
                            .build())
                    .build());
        };
    }
}
