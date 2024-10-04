package uz.uzum.gateway.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody AuthRequest authRequest) {

        System.out.println("Received email: " + authRequest.getEmail());
        System.out.println("Received password: " + authRequest.getPassword());

        return authService.createToken(authRequest.getEmail(), authRequest.getPassword())
                .map(token -> new ResponseEntity<>("JWT token: " + token, HttpStatus.ACCEPTED));  // Return a Mono<ResponseEntity<String>>
    }

}
