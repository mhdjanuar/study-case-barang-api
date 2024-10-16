package com.mhdjanuar.crudspringboot11.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUpload {
    private static final String UPLOAD_DIR = "uploads/";

    public static Path uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);

        // Create directories if they don't exist
        Files.createDirectories(filePath.getParent());
        file.transferTo(filePath); // Transfer the file to the desired location

        return filePath; // Return the path of the uploaded file
    }
}
