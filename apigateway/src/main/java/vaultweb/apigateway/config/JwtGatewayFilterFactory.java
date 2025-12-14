package vaultweb.apigateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilterChain;

@Configuration
public class JwtGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public JwtGatewayFilterFactory(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            // Create a WebFilterChain adapter from GatewayFilterChain
            WebFilterChain webFilterChain = webExchange -> chain.filter(exchange);

            return jwtAuthenticationFilter.filter(exchange, webFilterChain);
        };
    }
}

