package com.transport.transport_api.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "driver_profile")
public class DriverProfile {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @Column(unique = true, nullable = false)
    private String licenseNumber;

    private String idCardUrl;
    private String licenceUrl;

    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus;

    private Double ratingScore;

    private boolean isOnline;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point lastLocation;

    @OneToMany(mappedBy = "driverProfile", fetch = FetchType.LAZY)
    private List<Vehicle> vehicles;



}
