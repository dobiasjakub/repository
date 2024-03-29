package com.dobiasdev.spa.controllers.api;

import com.dobiasdev.spa.security.payload.AuthenticationRequest;
import com.dobiasdev.spa.security.payload.AuthenticationResponse;
import com.dobiasdev.spa.security.AuthenticationService;
import com.dobiasdev.spa.security.payload.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        System.out.println(request);
        return ResponseEntity.ok(service.authenticate(request));
    }
}
