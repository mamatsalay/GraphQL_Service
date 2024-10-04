package uz.uzum.gateway.model;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return userRepository.findByEmail(email)
                .map(user -> (UserDetails) new User(user.getUsername(), user.getPassword(), user.getAuthorities()))  // Convert Users entity to UserDetails
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found with email: " + email)));
    }

    public Mono<Boolean> validateUser(String email, String password) {
        return userRepository.findByEmail(email)
                .flatMap(user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.just(true);
                    } else {
                        return Mono.error(new UsernameNotFoundException("Invalid credentials for email: " + email));
                    }
                })
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found with email: " + email)));
    }
}
