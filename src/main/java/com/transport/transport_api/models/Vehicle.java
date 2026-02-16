package com.transport.transport_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // RELATION : Plusieurs véhicules appartiennent à UN chauffeur
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_profile_id", nullable = false)
    private DriverProfile driverProfile;

    @Column(nullable = false, unique = true)
    private String plateNumber; // Matricule (ex: 00123-116-16)

    @Column(nullable = false)
    private String model; // Marque + Modèle (ex: Toyota Hilux)

    private String color; // Utile pour le client ("Cherchez le fourgon blanc")

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType type; // Enum défini plus haut

    // LOGISTIQUE
    private Double volumeM3; // Capacité (ex: 12.5) - Nullable car une moto n'a pas de volume cubique pertinent

    private String photoUrl; // Photo du véhicule pour validation
}