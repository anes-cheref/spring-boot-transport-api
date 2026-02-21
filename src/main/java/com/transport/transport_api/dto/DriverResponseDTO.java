package com.transport.transport_api.dto;

public record DriverResponseDTO(String licensenumber,String idCardUrl,String licenseURL,String vehicleModel, String vehiclePlate, String VehicleType, Double ratingScore, String verificationStatus) {
}
