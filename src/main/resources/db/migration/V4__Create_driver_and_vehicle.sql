-- Activer l'extension PostGIS (OBLIGATOIRE pour le GPS)
CREATE EXTENSION IF NOT EXISTS postgis;

-- Création de la table DRIVER_PROFILE
CREATE TABLE driver_profile (
                                id UUID PRIMARY KEY,
                                user_id UUID NOT NULL UNIQUE, -- Lien OneToOne
                                license_number VARCHAR(50) NOT NULL UNIQUE,
                                id_card_url VARCHAR(255),
                                license_url VARCHAR(255),
                                verification_status VARCHAR(20) DEFAULT 'PENDING', -- Enum
                                rating_score DOUBLE PRECISION, -- Peut être NULL au début
                                is_online BOOLEAN DEFAULT FALSE,
                                last_location GEOMETRY(Point, 4326),

                                CONSTRAINT fk_driver_user FOREIGN KEY (user_id) REFERENCES _user(id) ON DELETE CASCADE
);

-- Création de l'INDEX SPATIAL (Vital pour les perfs de "findNearbyDrivers")
CREATE INDEX idx_driver_location ON driver_profile USING GIST (last_location);

-- Création de la table VEHICLE
CREATE TABLE vehicle (
                         id UUID PRIMARY KEY,
                         driver_profile_id UUID NOT NULL, -- Lien ManyToOne
                         plate_number VARCHAR(20) NOT NULL UNIQUE,
                         model VARCHAR(100) NOT NULL,
                         color VARCHAR(50),
                         type VARCHAR(30) NOT NULL, -- Enum (FOURGON, MOTO...)
                         volume_m3 DOUBLE PRECISION,
                         picture_url VARCHAR(255),

                         CONSTRAINT fk_vehicle_driver FOREIGN KEY (driver_profile_id) REFERENCES driver_profile(id) ON DELETE CASCADE
);