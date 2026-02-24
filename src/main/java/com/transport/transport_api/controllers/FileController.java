package com.transport.transport_api.controllers;

import com.transport.transport_api.services.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileStorageService.uploadFile(file);

        // Renvoie un JSON : {"url": "http://..."} que le front réutilisera
        return ResponseEntity.ok(Map.of("url", fileUrl));
    }
}