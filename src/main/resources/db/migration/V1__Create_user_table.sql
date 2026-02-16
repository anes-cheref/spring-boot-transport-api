CREATE TABLE _user (
    -- L'ID est un String
                       id VARCHAR(255) NOT NULL,

    -- Les champs String classiques
                       full_name VARCHAR(255),
                       email VARCHAR(255),
                       password VARCHAR(255),

    -- Le numéro de téléphone (Unique et Non Null comme demandé)
                       phone_number VARCHAR(255) NOT NULL,

    -- L'Enum Role est stocké en String ("CLIENT", "DRIVER")
                       role VARCHAR(50),

    -- Les champs pour l'OTP
                       otp_code VARCHAR(10),
                       otp_expiry_date TIMESTAMP,

    -- Le booléen (Postgres utilise BOOLEAN)
                       is_verified BOOLEAN NOT NULL DEFAULT FALSE,

    -- Définition de la clé primaire
                       PRIMARY KEY (id)
);

-- Ajout de la contrainte d'unicité sur le téléphone (Sécurité DB)
ALTER TABLE _user ADD CONSTRAINT uc_user_phone_number UNIQUE (phone_number);