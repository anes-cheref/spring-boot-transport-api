package com.transport.transport_api.dto;

import com.transport.transport_api.models.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(
        // Point de départ
        @NotNull double pickupLat,
        @NotNull double pickupLng,
        @NotBlank String pickupAddress,

        // Point d'arrivée
        @NotNull double dropOffLat,
        @NotNull double dropOffLng,
        @NotBlank String dropOffAddress,

        // Type de véhicule demandé (ex: FOURGON)
        @NotNull VehicleType requestedVehicleType
) {}