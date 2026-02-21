package com.transport.transport_api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserDetailsService userDetailsService; // Injecté pour l'AuthenticationProvider

    // Le "Hacheur" de mot de passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Le "Chef de la Sécurité"
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // On utilise le paramètre 'config' injecté par Spring, pas un 'new'
        return config.getAuthenticationManager();
    }

    // Le "Fournisseur d'Authentification" (C'est lui qui fait le lien BDD <-> Login)
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Il a besoin de ton service pour trouver l'user
        authProvider.setPasswordEncoder(passwordEncoder()); // Il a besoin de ton encodeur pour vérifier le mdp
        return authProvider;
    }
}