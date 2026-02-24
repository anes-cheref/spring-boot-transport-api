-- ==============================================================================
-- 1. TABLE DES COMMANDES (orders)
-- ==============================================================================
CREATE TABLE orders (
                        id UUID PRIMARY KEY,
                        user_id UUID NOT NULL, -- Le client qui commande
                        driver_id UUID,        -- Le chauffeur (NULL au moment de la commande)

    -- Coordonnées GPS (PostGIS)
                        pickup_location GEOMETRY(Point, 4326) NOT NULL,
                        drop_off_location GEOMETRY(Point, 4326) NOT NULL,

    -- Adresses textuelles pour l'affichage mobile
                        pickup_address VARCHAR(255),
                        drop_off_address VARCHAR(255),

    -- Logistique et statuts
                        requested_vehicle_type VARCHAR(50) NOT NULL,
                        current_status VARCHAR(50) NOT NULL,

    -- Argent : On utilise NUMERIC(10, 2) pour correspondre au BigDecimal Java
    -- (10 chiffres au total, dont 2 après la virgule)
                        estimated_price NUMERIC(10, 2),
                        final_price NUMERIC(10, 2),

    -- Audit
                        created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),

    -- Clés étrangères
    -- ⚠️ Vérifie bien que ta table utilisateur s'appelle "_user" (ou change en "users" / "app_user")
                        CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES _user(id) ON DELETE CASCADE,
                        CONSTRAINT fk_order_driver FOREIGN KEY (driver_id) REFERENCES driver_profile(id) ON DELETE SET NULL
);

-- Index spatial pour chercher rapidement les commandes autour d'un chauffeur
CREATE INDEX idx_order_pickup_location ON orders USING GIST (pickup_location);


-- ==============================================================================
-- 2. TABLE D'HISTORIQUE DES STATUTS (order_status_history)
-- ==============================================================================
CREATE TABLE order_status_history (
                                      id UUID PRIMARY KEY,
                                      order_id UUID NOT NULL,
                                      order_status VARCHAR(50) NOT NULL,
                                      changed_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),

    -- On stocke la position du chauffeur au moment où il a changé le statut
                                      location_at_change GEOMETRY(Point, 4326),

    -- Clé étrangère vers la commande
                                      CONSTRAINT fk_history_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

-- Index classique pour récupérer rapidement l'historique d'une commande spécifique
CREATE INDEX idx_history_order_id ON order_status_history(order_id);