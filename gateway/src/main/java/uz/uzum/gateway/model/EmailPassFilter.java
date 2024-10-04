package uz.uzum.gateway.model;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class EmailPassFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(Objects::nonNull)
                .flatMap(authentication -> {
                    // Get the email from the authentication principal
                    String email = authentication.getName();

                    // Debugging: Log the email
                    System.out.println("AddEmailHeaderFilter - Email from authentication: " + email);

                    // Mutate the request to add the email header
                    ServerHttpRequest mutatedRequest = exchange.getRequest()
                            .mutate()
                            .header("X-User-Email", email)
                            .build();

                    // Build a new exchange with the mutated request
                    ServerWebExchange mutatedExchange = exchange.mutate()
                            .request(mutatedRequest)
                            .build();

                    // Continue the filter chain with the mutated exchange
                    return chain.filter(mutatedExchange);
                })
                .switchIfEmpty(chain.filter(exchange));  // If authentication is null, continue without modifying the request
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;  // Ensure this runs after authentication has been set
    }
}
