package com.dobiasdev.spa.security;

import com.dobiasdev.spa.persistance.entities.User;
import com.dobiasdev.spa.persistance.enums.Role;
import com.dobiasdev.spa.repositories.UserRepository;
import com.dobiasdev.spa.security.payload.AuthenticationRequest;
import com.dobiasdev.spa.security.payload.AuthenticationResponse;
import com.dobiasdev.spa.security.payload.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.User)
                .created(LocalDateTime.now())
                .build();
        System.out.println(user);
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .userId(user.getId())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        System.out.println("authenticate");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        System.out.println("authenticated");
        var user = repository.findByEmailOrUserName(request.getUsername()).orElseThrow();
        System.out.println(user.getUsername());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .username(request.getUsername())
                .build();
    }
}
