package com.transport.transport_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders") // ⚠️ "orders" au pluriel pour éviter le bug SQL
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // nullable=false car on a toujours un client
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id") // Peut être NULL au début (PENDING)
    private DriverProfile driver;

    // --- Géographie ---
    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point pickupLocation;

    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point dropOffLocation;

    private String pickupAddress;  // Ex: "Gare de Lyon, Paris"
    private String dropOffAddress; // Ex: "Tour Eiffel, Paris"

    // --- Logistique & Argent ---
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType requestedVehicleType;

    @Column(precision = 10, scale = 2) // Pour la base de données (ex: 99999999.99)
    private BigDecimal estimatedPrice; // Utilisation de BigDecimal

    @Column(precision = 10, scale = 2)
    private BigDecimal finalPrice; // Sera null jusqu'à la fin de la course

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus currentStatus;

    // --- Audit ---
    @CreationTimestamp // Spring va remplir ça tout seul à la création !
    private LocalDateTime createdAt;
}