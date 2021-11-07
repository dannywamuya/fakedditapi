package me.dwamuya.fakeddit.services;

import lombok.AllArgsConstructor;
import me.dwamuya.fakeddit.dto.RegisterRequest;
import me.dwamuya.fakeddit.exceptions.CustomException;
import me.dwamuya.fakeddit.mapper.UserMapper;
import me.dwamuya.fakeddit.models.NotificationEmail;
import me.dwamuya.fakeddit.models.User;
import me.dwamuya.fakeddit.models.VerificationToken;
import me.dwamuya.fakeddit.repositories.UserRepository;
import me.dwamuya.fakeddit.repositories.VerificationTokenRepository;
import me.dwamuya.fakeddit.security.AuthStatus;
import me.dwamuya.fakeddit.security.AuthenticationRequest;
import me.dwamuya.fakeddit.security.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final UDService udService;
    private final JWTUtil jwtUtil;
    private final AuthStatus authStatus;
    private final UserMapper userMapper;
    private final UserService userService;

    public Object signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setActive(true);
        user.setCreatedAt(Instant.now());

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new CustomException("Unable to save user " + user);
        }

//        String token = generateVerificationToken(user);
//
//        mailService.sendMail(new NotificationEmail(
//           "Please activate your account",
//           user.getEmail(),
//                "Thank you for joining Fakeddit. " +
//                        "Click on the url below to activate your account : " +
//                        "http://localhost:8080/api/auth/verify/" + token
//        ));

        return "User registration successful";
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(
                new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 12))
        );

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public void verify(String token) {
        VerificationToken verificationToken =
                verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new CustomException("Invalid Token"));

        fetchUserAndEnable(verificationToken);
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("User of username : " + username + " not found"));

        user.setActive(true);
        userRepository.save(user);
    }

    public String authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new CustomException("Incorrect username or password");
        }

        final UserDetails ud = udService
                .loadUserByUsername(authenticationRequest.getUsername());

        return jwtUtil.generateToken(ud);
    }

    public Object currentlyLoggedIn() {
        if (authStatus.getUser().getUserId() != null) {
          return userMapper.toResponse(authStatus.getUser());
        }
        return null;
    }

    public Object getUserByUsername(String username) {
        return userMapper.toResponse(userService.findUserByUsername(username));
    }
}