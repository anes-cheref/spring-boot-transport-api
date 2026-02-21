package com.transport.transport_api.services;


import com.transport.transport_api.dto.AuthenticationResponse;
import com.transport.transport_api.dto.LoginDTO;
import com.transport.transport_api.dto.RegisterDTO;
import com.transport.transport_api.models.Role;
import com.transport.transport_api.models.User;
import com.transport.transport_api.repositories.UserRepository;
import com.transport.transport_api.security.SmsSender;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final SmsSender smsSender;

    // --- 1. REGISTER (Blindé) ---
    public String register(RegisterDTO request) {
        // A. On vérifie si l'utilisateur existe déjà
        var existingUserOpt = repository.findByPhoneNumber(request.phoneNumber());

        User user;

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            // CAS 1 : Il existe ET il est déjà vérifié -> Erreur
            if (existingUser.isVerified()) {
                throw new RuntimeException("Ce numéro est déjà inscrit et actif. Veuillez vous connecter.");
            }
            // CAS 2 : Il existe mais N'EST PAS vérifié (a raté son inscription précédente)
            // -> On va mettre à jour cet utilisateur au lieu de bloquer
            user = existingUser;
            user.setFullName(request.fullName());
            user.setPassword(passwordEncoder.encode(request.password())); // On met à jour le mdp au cas où il l'a changé
            user.setRole(request.role());
        } else {
            // CAS 3 : Nouvel utilisateur
            user = User.builder()
                    .phoneNumber(request.phoneNumber())
                    .password(passwordEncoder.encode(request.password()))
                    .fullName(request.fullName())
                    .role(request.role())
                    .isVerified(false)
                    .build();
        }

        // B. Génération OTP (SecureRandom est mieux)
        String code = generateOTP();
        user.setOtpCode(code);
        user.setOtpExpiryDate(LocalDateTime.now().plusMinutes(5));

        // C. Sauvegarde & Envoi
        repository.save(user);
        smsSender.sendSms(request.phoneNumber(), "Votre code Wassel est : " + code);

        return "Code envoyé !";
    }

    // --- 2. AUTHENTICATE (Sécurisé) ---
    public AuthenticationResponse authenticate(LoginDTO request) {
        // A. Vérification Login/Mdp standard
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.phoneNumber(), request.password())
        );

        // B. Récupération de l'user
        var user = repository.findByPhoneNumber(request.phoneNumber())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // C. VERIFICATION CRUCIALE : Est-ce qu'il a validé son SMS ?
        if (!user.isVerified()) {
            throw new RuntimeException("Votre compte n'est pas vérifié. Veuillez valider votre code SMS.");
        }

        // D. Tout est bon, on donne le Token
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    // --- 3. VERIFY (Rien à changer, c'était parfait) ---
    public AuthenticationResponse verify(String phone, String codeInput) {
        User user = repository.findByPhoneNumber(phone)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (user.getOtpCode() == null || !user.getOtpCode().equals(codeInput)) {
            throw new RuntimeException("Code invalide");
        }
        if (user.getOtpExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Code expiré. Veuillez recommencer l'inscription.");
        }

        user.setVerified(true);
        user.setOtpCode(null);
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    // Petit helper pour générer le code proprement
    private String generateOTP() {
        // SecureRandom est plus sûr pour la sécurité
        java.security.SecureRandom random = new java.security.SecureRandom();
        int code = 1000 + random.nextInt(9000); // Entre 1000 et 9999
        return String.valueOf(code);
    }
}