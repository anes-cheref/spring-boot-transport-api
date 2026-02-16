package com.transport.transport_api.services;

import com.transport.transport_api.dto.DriverOnBoardingDTO;
import com.transport.transport_api.dto.DriverPublicDTO;
import com.transport.transport_api.models.*;
import com.transport.transport_api.repositories.DriverProfileRepository;
import com.transport.transport_api.repositories.VehicleRepository;
import com.transport.transport_api.utils.GeometryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverProfileRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    // CREATION (Avec Véhicule !) ---
    @Transactional // Si le véhicule plante, le driver est annulé aussi (Tout ou rien)
    public String createDriverProfile(User user, DriverOnBoardingDTO request) {
        // Vérif préventive (optionnelle si gérée ailleurs)
        if (driverRepository.existsByUser(user)) {
            throw new IllegalArgumentException("Vous êtes déjà conducteur");
        }

        // Création du Chauffeur
        DriverProfile driver = DriverProfile.builder()
                .user(user)
                .licenseNumber(request.licenseNumber())
                .licenceUrl(request.licenseUrl())
                .idCardUrl(request.idCardUrl())
                .verificationStatus(VerificationStatus.PENDING)
                .ratingScore(null) // Pas encore de note
                .isOnline(false)
                .lastLocation(null) // On ne sait pas où il est à l'inscription
                .build();

        DriverProfile savedDriver = driverRepository.save(driver);

        // Création du Véhicule
        Vehicle vehicle = Vehicle.builder()
                .driverProfile(savedDriver) // On fait le lien !
                .plateNumber(request.vehiclePlate())
                .model(request.vehicleModel())
                .color(request.vehicleColor())
                .type(request.vehicleType())
                .build();

        vehicleRepository.save(vehicle);

        return savedDriver.getId().toString();
    }

    // VERIFICATION (Admin)
    public void verifyDriverProfile(UUID driverId, VerificationStatus newStatus) {
        DriverProfile driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Chauffeur introuvable"));

        driver.setVerificationStatus(newStatus);

        driverRepository.save(driver); // <--- NE PAS OUBLIER CA !
    }

    // ONLINE/OFFLINE
    public void toggleOnlineStatus(UUID driverId) {
        DriverProfile driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Chauffeur introuvable"));

        // Vérification logique
        if (driver.getVerificationStatus() != VerificationStatus.VERIFIED) {
            throw new RuntimeException("Votre compte n'est pas encore validé !");
        }

        driver.setOnline(!driver.isOnline());

        driverRepository.save(driver);
    }

    // GPS UPDATE
    public void updateLocation(UUID driverId, double latitude, double longitude) {
        DriverProfile driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Chauffeur introuvable"));

        driver.setLastLocation(GeometryUtil.createPoint(latitude, longitude));

        driverRepository.save(driver);
    }

    // RECHERCHE
    public List<DriverPublicDTO> findNearbyDrivers(double lat, double lon, double radiusKm) {
        Point clientLocation = GeometryUtil.createPoint(lat, lon);

        List<DriverProfile> drivers = driverRepository.findDriversWithinDistance(clientLocation, radiusKm * 1000);

        return drivers.stream()
                .map(driver -> {
                    // Gestion sécurisée du véhicule (au cas où il n'en a pas)
                    Vehicle activeVehicle = (driver.getVehicles() != null && !driver.getVehicles().isEmpty())
                            ? driver.getVehicles().get(0)
                            : null;

                    String model = (activeVehicle != null) ? activeVehicle.getModel() : "Inconnu";
                    // Attention: activeVehicle.getType() renvoie un ENUM, il faut .name()
                    String type = (activeVehicle != null) ? activeVehicle.getType().name() : "STANDARD";
                    String color = (activeVehicle != null) ? activeVehicle.getColor() : "Blanc";

                    // Conversion propre pour le frontend
                    return new DriverPublicDTO(
                            driver.getId(),
                            driver.getUser().getFullName(),
                            driver.getUser().getPictureUrl(),
                            model,
                            driver.getRatingScore(),
                            color,
                            type,
                            driver.getLastLocation().getY(), // Latitude (Y)
                            driver.getLastLocation().getX()  // Longitude (X)
                    );
                })
                .toList();
    }
}