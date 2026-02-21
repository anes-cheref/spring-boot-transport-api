package com.transport.transport_api.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtil {

    // SRID 4326 est le standard GPS (WGS84) utilisé par Google Maps
    private static final GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);

    public static Point createPoint(double latitude, double longitude) {
        // Attention : JTS prend (x, y) donc (longitude, latitude)
        return factory.createPoint(new Coordinate(longitude, latitude));
    }
}