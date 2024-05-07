package com.example.cloudservice.service;

import com.example.cloudservice.auth.AuthenticationRequest;
import com.example.cloudservice.auth.AuthenticationResponse;
import com.example.cloudservice.auth.RegisterRequest;
import com.example.cloudservice.config.UserDetailsImpl;
import com.example.cloudservice.entity.MyUser;
import com.example.cloudservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        String jwtToken = jwtService.generateToken(userDetails);
        return new AuthenticationResponse(jwtToken);
    }
}
