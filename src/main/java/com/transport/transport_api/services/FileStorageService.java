package com.transport.transport_api.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    // Dossier temporaire sur ton PC pour tester.
    // Plus tard : Amazon S3 ou Cloudinary.
    private final String UPLOAD_DIR = "uploads/";

    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Le fichier est vide !");
        }

        try {
            // Créer le dossier s'il n'existe pas
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Générer un nom unique pour éviter d'écraser des fichiers (ex: 1234-5678-image.jpg)
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            // Sauvegarder sur le disque
            Files.write(filePath, file.getBytes());

            // On simule l'URL que Cloudinary/S3 te renverrait (à adapter plus tard)
            return "http://localhost:8080/uploads/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du fichier", e);
        }
    }
}