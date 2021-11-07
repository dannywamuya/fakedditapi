package me.dwamuya.fakeddit.controllers;

import lombok.AllArgsConstructor;
import me.dwamuya.fakeddit.dto.RegisterRequest;
import me.dwamuya.fakeddit.security.AuthenticationRequest;
import me.dwamuya.fakeddit.security.AuthenticationResponse;
import me.dwamuya.fakeddit.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp (@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.signup(registerRequest));
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity<?> verify (@PathVariable String token) {
        authService.verify(token);
        return ResponseEntity.ok("User Verification Successful");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate (
            @RequestBody AuthenticationRequest authenticationRequest
    ) {
        return ResponseEntity.ok(
                new AuthenticationResponse(authService.authenticate(authenticationRequest))
        );
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> currentlyLoggedIn() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.currentlyLoggedIn());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserById(
            @PathVariable(name = "username") String username
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.getUserByUsername(username));
    }

}