package com.transport.transport_api.repositories;

import com.transport.transport_api.models.DriverProfile;
import com.transport.transport_api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.locationtech.jts.geom.Point;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DriverProfileRepository extends JpaRepository<DriverProfile, UUID> {
    boolean existsByUser(User user);
    Optional<DriverProfile> findByUser(User user);
    @Query(value = """
        SELECT * FROM driver_profile d
        WHERE d.is_online = true 
        AND d.verification_status = 'VERIFIED'
        AND ST_DWithin(
            d.last_location, 
            :point, 
            :radiusInMeters
        )
        """, nativeQuery = true)
    List<DriverProfile> findDriversWithinDistance(
            @Param("point") Point point,
            @Param("radiusInMeters") double radiusInMeters
    );

}
