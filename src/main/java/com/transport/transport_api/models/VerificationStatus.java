package com.transport.transport_api.models;

public enum VerificationStatus {
    PENDING,  // En attente
    VERIFIED, // Validé par l'admin
    REJECTED  // Refusé (photo floue, etc.)
}