package com.transport.transport_api.dto;

import com.transport.transport_api.models.OrderStatus;
import com.transport.transport_api.models.VehicleType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        String orderId,

        // Infos du client
        String clientName,

        // Infos du chauffeur (Peut être null si PENDING)
        String driverId,
        String driverName,
        String driverPhone, // Pratique pour que le client l'appelle

        // Adresses
        String pickupAddress,
        String dropOffAddress,

        // Logistique & Argent
        VehicleType vehicleType,
        OrderStatus status,
        BigDecimal estimatedPrice,
        BigDecimal finalPrice,

        LocalDateTime createdAt
) {}