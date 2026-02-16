package com.transport.transport_api.controllers;

import com.transport.transport_api.dto.DriverOnBoardingDTO;
import com.transport.transport_api.dto.DriverPublicDTO;
import com.transport.transport_api.models.User;
import com.transport.transport_api.models.VerificationStatus;
import com.transport.transport_api.services.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/drivers") // "drivers" au pluriel est une convention REST
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    // DEVENIR CHAUFFEUR (Onboarding)
    @PostMapping
    public ResponseEntity<String> createDriverProfile(
            @AuthenticationPrincipal User currentUser, // Récupère le User via le Token JWT
            @RequestBody @Valid DriverOnBoardingDTO request // Récupère le JSON
    ) {
        return ResponseEntity.ok(driverService.createDriverProfile(currentUser, request));
    }

    // VERIFIER UN CHAUFFEUR (Admin seulement)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/verify") // Patch car on modifie juste une partie (le status)
    public ResponseEntity<Void> verifyDriverProfile(
            @PathVariable("id") UUID driverId,
            @RequestParam VerificationStatus status // ex: ?status=VERIFIED
    ) {
        driverService.verifyDriverProfile(driverId, status);
        return ResponseEntity.ok().build();
    }

    // 3. ONLINE / OFFLINE (Chauffeur connecté seulement)
    @PostMapping("/status")
    public ResponseEntity<Void> toggleOnlineStatus(
            @AuthenticationPrincipal User currentUser
    ) {
        driverService.toggleOnlineStatusByUser(currentUser);
        return ResponseEntity.ok().build();
    }

    // MISE A JOUR GPS (Appelée par l'appli toutes les 10s)
    @PatchMapping("/location")
    public ResponseEntity<Void> updateLocation(
            @AuthenticationPrincipal User currentUser,
            @RequestParam double lat,
            @RequestParam double lng
    ) {
        // Idem, on utilise le User connecté pour la sécurité
        driverService.updateLocationByUser(currentUser, lat, lng);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/nearby")
    public ResponseEntity<List<DriverPublicDTO>> findNearbyDrivers(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "5.0") double radiusKm // 5km par défaut
    ) {
        return ResponseEntity.ok(driverService.findNearbyDrivers(lat, lon, radiusKm));
    }
}