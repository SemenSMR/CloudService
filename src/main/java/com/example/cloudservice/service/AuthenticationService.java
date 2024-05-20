package com.example.cloudservice.service;

import com.example.cloudservice.auth.AuthenticationRequest;
import com.example.cloudservice.auth.AuthenticationResponse;
import com.example.cloudservice.auth.LoginResponse;
import com.example.cloudservice.auth.RegisterRequest;
import com.example.cloudservice.config.UserDetailsImpl;
import com.example.cloudservice.entity.MyUser;
import com.example.cloudservice.exception.UsernameNotFoundException;
import com.example.cloudservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = MyUser.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        repository.save(user);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        String jwtToken = jwtService.generateToken(userDetails);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getLogin())
                .orElseThrow();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        String jwtToken = jwtService.generateToken(userDetails);
        return new AuthenticationResponse(jwtToken);
    }

    public ResponseEntity<?> login(AuthenticationRequest request) {
        String username = request.getLogin();
        String password = request.getPassword();
        MyUser user = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse( "Invalid username or password"));
        }
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        String jwtToken = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponse(jwtToken));
    }

}

