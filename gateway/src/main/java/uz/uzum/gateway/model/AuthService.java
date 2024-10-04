package uz.uzum.gateway.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uz.uzum.gateway.model.AuthRequest;

@Service
public class AuthService {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    public AuthService(CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public Mono<String> createToken(String email, String password) {
        System.out.println("AuthService.createToken - Email: " + email + ", Password: " + password);
        return customUserDetailsService.validateUser(email, password)
                .flatMap(isValid -> {
                    if (isValid) {
                        return customUserDetailsService.findByUsername(email)
                                .flatMap(userDetails -> Mono.just(jwtUtil.generateToken(userDetails)));
                    } else {
                        return Mono.just("Invalid credentials");
                    }
                });
    }

}
