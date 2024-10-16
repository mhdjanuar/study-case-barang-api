package com.mhdjanuar.crudspringboot11.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhdjanuar.crudspringboot11.domain.Barang;
import com.mhdjanuar.crudspringboot11.exception.InvalidFileTypeException;
import com.mhdjanuar.crudspringboot11.exception.ResourceNotFoundException;
import com.mhdjanuar.crudspringboot11.repository.BarangRepository;
import com.mhdjanuar.crudspringboot11.service.BarangService;
import com.mhdjanuar.crudspringboot11.utils.FileUpload;

@Service
public class BarangServiceImpl implements BarangService {

    @Autowired
    private BarangRepository barangRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void createNewBarang(
        String namaBarang, 
        String jumlahBarang,
        String additionalInfoJson, 
        MultipartFile file
    ) throws InvalidFileTypeException {
        List<String> allowedMimeTypes = Arrays.asList("image/jpeg", "image/png");

        try {
            // Validasi MIME Type
            String mimeType = file.getContentType();
            if (!allowedMimeTypes.contains(mimeType)) {
                throw new InvalidFileTypeException("File must be a JPG or PNG image.");
            }

            // Konversi JSON string ke Map
            Map<String, Object> additionalInfoMap = objectMapper.readValue(additionalInfoJson, Map.class);
            String additionalInfo = objectMapper.writeValueAsString(additionalInfoMap); // Konversi kembali ke String untuk disimpan di jsonb

            String nomorSeriBarang = UUID.randomUUID().toString();
            Path filePath = FileUpload.uploadFile(file);

            // Menyimpan entitas Barang dengan Native Query
            barangRepository.saveNative(
                namaBarang,
                Integer.parseInt(jumlahBarang), 
                nomorSeriBarang, 
                additionalInfo,
                filePath.toString(),
                "admin",
                LocalDateTime.now()
            );

        
        } catch (JsonMappingException e) {
            throw new RuntimeException("Invalid JSON format: " + e.getMessage(), e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("I/O error: " + e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace(); // Menangani pengecualian lain jika perlu
            throw e; // Lempar kembali pengecualian
        }
    }

    @Override
    public List<Barang> getAllBarang() {
        return barangRepository.findAll();
    }

    @Override
    public Barang getBarangById(Long id) {
        Optional<Barang> barangOptional = barangRepository.findById(id); // Assuming your repository extends JpaRepository
        
        if (barangOptional.isPresent()) {
            return barangOptional.get();
        } else {
            throw new ResourceNotFoundException("Barang not found with ID: " + id); // Create this exception class
        }
    }

    @Override
    public void updateBarang(Long id, String namaBarang, String jumlahBarang, String additionalInfoJson, MultipartFile file) {
        try {
            Optional<Barang> barangOptional = barangRepository.findById(id);
            if (!barangOptional.isPresent()) {
                throw new ResourceNotFoundException("Barang not found with ID: " + id);
            }
    
            String gambarBarang = barangOptional.get().getGambarBarang(); // Mengambil path gambar yang ada
    
            // Tangani unggahan file jika file baru disediakan
            if (file != null && !file.isEmpty()) {
                // Logika unggahan file di sini
                // Misalnya, menyimpan gambar baru dan memperbarui gambarBarang
                Path filePath = FileUpload.uploadFile(file);
                gambarBarang = filePath.toString(); // Memperbarui path gambar
            }

            // Konversi JSON string ke Map
            Map<String, Object> additionalInfoMap = objectMapper.readValue(additionalInfoJson, Map.class);
            // Konversi kembali ke String untuk disimpan di jsonb
            String additionalInfo = objectMapper.writeValueAsString(additionalInfoMap);
            
            // Memanggil metode pembaruan native
            barangRepository.updateBarangNative(id, namaBarang, Integer.parseInt(jumlahBarang), "", additionalInfo, gambarBarang, LocalDateTime.now());
        } catch (Exception e) {
            throw new RuntimeException("Error processing additionalInfo: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteBarang(Long id) {
        // Periksa apakah barang ada sebelum menghapus
        if (!barangRepository.existsById(id)) {
            throw new ResourceNotFoundException("Barang not found with ID: " + id);
        }
        
        // Panggil metode penghapusan
        barangRepository.deleteById(id);
    }
    
}
