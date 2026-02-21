package com.transport.transport_api.services;


import com.transport.transport_api.dto.VehicleDTO;
import com.transport.transport_api.models.DriverProfile;
import com.transport.transport_api.models.Vehicle;
import com.transport.transport_api.repositories.DriverProfileRepository;
import com.transport.transport_api.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final DriverProfileRepository driverRepository;

    public Vehicle addVehicle(UUID driverId,VehicleDTO vehicle) {
        DriverProfile driver = driverRepository.findById(driverId).orElseThrow(() -> new RuntimeException("Chauffeur introuvable"));
        Vehicle vehicule = Vehicle.builder().
                id(UUID.randomUUID()).
                model(vehicle.model()).
                color(vehicle.color()).
                type(vehicle.type()).
                driverProfile(driver).
                plateNumber(vehicle.plateNumber()).
                pictureUrl(vehicle.photoUrl()).
                volumeM3(vehicle.volumeM3()).
                build();
        driver.addVehicle(vehicule);
        return vehicleRepository.save(vehicule);
    }

    public Vehicle updateVehicle(UUID driverId, UUID VehicleId,VehicleDTO request) {
        DriverProfile driver = driverRepository.findById(driverId).orElseThrow(() -> new RuntimeException("Chauffeur introuvable"));
        Vehicle vehicle = vehicleRepository.findById(VehicleId).orElseThrow(() -> new RuntimeException("Vehicule introuvable"));

        if (!vehicle.getDriverProfile().getId().equals(driverId)) {
            throw new RuntimeException("Accès refusé : Ce véhicule ne vous appartient pas !");
        }

        vehicle.setPlateNumber(request.plateNumber());
        vehicle.setModel(request.model());
        vehicle.setColor(request.color());
        vehicle.setType(request.type());
        vehicle.setVolumeM3(request.volumeM3());

        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(UUID driverId, UUID vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Véhicule introuvable"));

        if (!vehicle.getDriverProfile().getId().equals(driverId)) {
            throw new RuntimeException("Accès refusé.");
        }
        DriverProfile driver = driverRepository.findById(driverId).orElseThrow(() -> new RuntimeException("Driver introuvable"));

        driver.removeVehicle(vehicle);
         vehicleRepository.delete(vehicle);
    }

    public List<Vehicle> getMyVehicles(UUID driverId) {
        DriverProfile driver = driverRepository.findById(driverId).orElseThrow();
        return vehicleRepository.findAllByDriverProfile(driver);
    }
}
