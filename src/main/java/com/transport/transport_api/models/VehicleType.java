package com.transport.transport_api.models;

public enum VehicleType {
    MOTO,           // Livraison rapide plis/colis
    CITADINE,       // Petite voiture (Maruti, Picanto...)
    BERLINE,        // Voiture standard (Symbol, Logan...)
    FOURGONNETTE,   // Type Partner, Berlingo, Caddy
    FOURGON,        // Type Master, Ducato, Boxer
    CAMION_BENNE,   // Pour gravats/sable
    CAMION_PLATEAU, // Pour dépannage ou gros objets
    CAMION_FRIGO    // Pour denrées périssables
}