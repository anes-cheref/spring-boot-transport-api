package com.transport.transport_api.controllers;


import com.transport.transport_api.dto.VehicleDTO;
import com.transport.transport_api.models.Vehicle;
import com.transport.transport_api.services.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping("/{driverId}")
    public ResponseEntity<Vehicle> addVehicle(
            @PathVariable UUID driverId,
            @RequestBody @Valid VehicleDTO request) {
        return ResponseEntity.ok(vehicleService.addVehicle(driverId, request));
    }

    @PostMapping("/{driverId}/{vehicleId}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable UUID driverId, @PathVariable UUID vehicleId, @RequestBody @Valid VehicleDTO request) {
        return ResponseEntity.ok(vehicleService.updateVehicle(driverId, vehicleId, request));
    }

    @DeleteMapping("{driverId}/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable UUID driverId, @PathVariable UUID vehicleId) {
        return ResponseEntity.noContent().build();
    }
}
