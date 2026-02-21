package com.transport.transport_api.repositories;

import com.transport.transport_api.models.DriverProfile;
import com.transport.transport_api.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    List<Vehicle> findAllByDriverProfile(DriverProfile driver);
}
