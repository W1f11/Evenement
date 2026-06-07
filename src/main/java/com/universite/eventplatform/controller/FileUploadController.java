package com.universite.eventplatform.controller;

import com.universite.eventplatform.exception.InvalidRequestException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@Tag(name = "Upload de fichiers")
public class FileUploadController {

    @Value("${app.upload.dir:./uploads/events}")
    private String uploadDir;

    @PostMapping("/image")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR', 'ORGANISATEUR')")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) throw new InvalidRequestException("Fichier vide.");

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/"))
            throw new InvalidRequestException("Seules les images sont acceptées.");

        try {
            Path dir = Paths.get(uploadDir);
            if (!Files.exists(dir)) Files.createDirectories(dir);

            String ext = "";
            String originalName = file.getOriginalFilename();
            if (originalName != null && originalName.contains("."))
                ext = originalName.substring(originalName.lastIndexOf("."));

            String filename = UUID.randomUUID().toString() + ext;
            Path destination = dir.resolve(filename);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            String url = "/uploads/events/" + filename;
            return ResponseEntity.ok(Map.of("imageUrl", url));
        } catch (IOException e) {
            throw new InvalidRequestException("Erreur lors de l'upload : " + e.getMessage());
        }
    }
}
