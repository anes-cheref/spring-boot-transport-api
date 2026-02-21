package com.transport.transport_api.dto;

import com.transport.transport_api.models.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehicleDTO(@NotBlank String plateNumber,
                         @NotBlank String model,
                         @NotBlank String color,
                         @NotNull VehicleType type,
                         Double volumeM3,
                         String photoUrl) {
}
