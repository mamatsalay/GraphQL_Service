package uz.uzum.gateway.model;

import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {


    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, @Lazy CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String jwtToken = authorization.substring(7);
            String email = jwtUtil.extractEmail(jwtToken);

            // Debugging: Log the extracted email
            System.out.println("JwtAuthenticationFilter - Extracted email: " + email);

            if (email != null) {
                return customUserDetailsService.findByUsername(email)
                        .flatMap(userDetails -> {
                            if (jwtUtil.validateToken(jwtToken, userDetails.getUsername())) {
                                UsernamePasswordAuthenticationToken authentication =
                                        new UsernamePasswordAuthenticationToken(
                                                userDetails, null, userDetails.getAuthorities());

                                // Mutate the request to add the email header
                                ServerHttpRequest mutatedRequest = exchange.getRequest()
                                        .mutate()
                                        .header("X-User-Email", email)  // Add the email as a custom header
                                        .build();

                                // Build a new exchange with the mutated request
                                ServerWebExchange mutatedExchange = exchange.mutate()
                                        .request(mutatedRequest)
                                        .build();

                                // Set authentication in the reactive security context
                                return chain.filter(mutatedExchange)
                                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                            } else {
                                // Token validation failed
                                return chain.filter(exchange);
                            }
                        });
            }
        }

        // Continue without authentication if no token is found
        return chain.filter(exchange);
    }
}
