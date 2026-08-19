package com.crowdfund.campaign;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {

    // Save inside src/main/resources/static/uploads so Spring Boot serves them at /uploads/*
    private final Path uploadPath;

    public FileUploadService() {
        try {
            // Try to save alongside the running classes in static/uploads for hot-reload access
            Path staticPath = Paths.get("src/main/resources/static/uploads");
            if (!Files.exists(staticPath)) {
                Files.createDirectories(staticPath);
            }
            // Also ensure the target/classes path exists so files are served immediately
            Path targetPath = Paths.get("target/classes/static/uploads");
            if (!Files.exists(targetPath)) {
                Files.createDirectories(targetPath);
            }
            // Fallback uploads dir at root
            Path fallbackPath = Paths.get("uploads");
            if (!Files.exists(fallbackPath)) {
                Files.createDirectories(fallbackPath);
            }
            this.uploadPath = targetPath;
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    public String saveFile(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            // Sanitize filename
            String safeName = originalFilename != null
                    ? originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_")
                    : "image";
            String fileName = UUID.randomUUID().toString() + "_" + safeName;

            // Save to target/classes/static/uploads (served immediately at /uploads/*)
            Path destTarget = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), destTarget, StandardCopyOption.REPLACE_EXISTING);

            // Also save to src/main/resources/static/uploads for persistence across recompiles
            Path destSrc = Paths.get("src/main/resources/static/uploads").resolve(fileName);
            Files.copy(destTarget, destSrc, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }
}
