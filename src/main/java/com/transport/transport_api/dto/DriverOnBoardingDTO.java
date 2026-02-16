package com.transport.transport_api.dto;

import com.transport.transport_api.models.VehicleType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record DriverOnBoardingDTO(String licenseNumber,
                                  String idCardUrl,
                                  String licenseUrl,
                                  String vehicleModel,
                                  String vehicleColor,
                                  VehicleType vehicleType,
                                  String vehiclePlate,
                                  String VehicleType) {
}
