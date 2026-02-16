package com.transport.transport_api.dto;

import com.transport.transport_api.models.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(
        @NotBlank(message = "Le téléphone est obligatoire")
        String phoneNumber,

        @NotBlank(message = "Le mot de passe est obligatoire")
        String password,

        @NotBlank(message = "Le nom complet est obligatoire")
        String fullName,

        @NotNull(message = "Vous devez choisir : CLIENT ou DRIVER")
        Role role
) {}