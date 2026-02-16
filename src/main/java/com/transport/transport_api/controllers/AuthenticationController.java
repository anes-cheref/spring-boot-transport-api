package com.transport.transport_api.controllers;

import com.transport.transport_api.dto.AuthenticationResponse;
import com.transport.transport_api.dto.LoginDTO;
import com.transport.transport_api.dto.RegisterDTO;
import com.transport.transport_api.dto.VerifyDTO; // <--- Import Important
import com.transport.transport_api.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    // 1. REGISTER : Renvoie un message texte (String), pas un Token
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO request) {
        return ResponseEntity.ok(service.register(request));
    }

    // 2. VERIFY : C'est ICI qu'on utilise le VerifyDTO
    // C'est cette méthode qui donne le Token après validation du SMS
    @PostMapping("/verify")
    public ResponseEntity<AuthenticationResponse> verify(@RequestBody VerifyDTO request) {
        return ResponseEntity.ok(service.verify(request.phoneNumber(), request.code()));
    }

    // 3. LOGIN : Renvoie le Token (si le compte est vérifié)
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody LoginDTO request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}