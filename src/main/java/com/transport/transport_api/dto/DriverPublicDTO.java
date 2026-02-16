package com.transport.transport_api.dto;
import com.transport.transport_api.models.VehicleType;
import org.locationtech.jts.geom.Point;

import java.util.UUID;

public record DriverPublicDTO(UUID id,
                              String name,
                              String pictureUrl,
                              String vehicleModel,
                              Double ratingScore,
                              String VehicleColor,
                              String vehicleType,
                              Double lat,
                              Double lon
                              )
{}
