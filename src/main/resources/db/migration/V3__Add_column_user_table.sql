ALTER TABLE _user
    ADD is_driver BOOLEAN DEFAULT FALSE,
    ADD picture_url VARCHAR(255);