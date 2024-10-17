package com.mhdjanuar.crudspringboot11.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhdjanuar.crudspringboot11.domain.Barang;
import com.mhdjanuar.crudspringboot11.dto.BarangCreateDTO;
import com.mhdjanuar.crudspringboot11.dto.BarangResponseDTO;
import com.mhdjanuar.crudspringboot11.dto.BarangUpdateDTO;
import com.mhdjanuar.crudspringboot11.exception.InvalidFileTypeException;
import com.mhdjanuar.crudspringboot11.exception.ResourceNotFoundException;
import com.mhdjanuar.crudspringboot11.repository.BarangRepository;
import com.mhdjanuar.crudspringboot11.service.BarangService;
import com.mhdjanuar.crudspringboot11.utils.FileUpload;

@Service
public class BarangServiceImpl implements BarangService {
    private static final Logger logger = LogManager.getLogger(BarangServiceImpl.class);

    @Autowired
    private BarangRepository barangRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void createNewBarang(
        BarangCreateDTO barang,
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
            Map<String, Object> additionalInfoMap = objectMapper.readValue(barang.getAdditionalInfo(), Map.class);
            String additionalInfo = objectMapper.writeValueAsString(additionalInfoMap); // Konversi kembali ke String untuk disimpan di jsonb

            String nomorSeriBarang = UUID.randomUUID().toString();
            Path filePath = FileUpload.uploadFile(file);

            // Log request sebelum disimpan
            logger.info("Saving Barang request: namaBarang={}, jumlahBarang={}, additionalInfo={}, fileName={}", 
                barang.getNamaBarang(), 
                barang.getJumlahBarang(), 
                additionalInfo, 
                file.getOriginalFilename());

            // Menyimpan entitas Barang dengan Native Query
            barangRepository.saveNative(
                barang.getNamaBarang(),
                Integer.parseInt(barang.getJumlahBarang()), 
                nomorSeriBarang, 
                additionalInfo,
                filePath.toString(),
                "admin",
                LocalDateTime.now()
            );

            logger.info("Barang created successfully with name: test {}", barang.getNamaBarang());
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
    public List<BarangResponseDTO> getAllBarang() {
        List<Barang> barangs = barangRepository.findAll();

        logger.info("Barang list retrieved: {}", barangs);
        
        // Konversi dari Barang ke BarangResponseDTO
        return barangs.stream().map((b) -> {
            BarangResponseDTO dto = new BarangResponseDTO();
            dto.setId(b.getId());
            dto.setJumlahStokBarang(b.getJumlahStokBarang());
            dto.setNomorSeriBarang(b.getNomorSeriBarang());
            dto.setGambarBarang(b.getGambarBarang());
            dto.setNamaBarang(b.getNamaBarang());
            dto.setAdditionalInfo(b.getAdditionalInfo());
            dto.setCreatedAt(b.getCreatedAt());
            dto.setCreatedBy(b.getCreatedBy());
            dto.setUpdatedAt(b.getUpdatedAt());
            dto.setUpdatedBy(b.getUpdatedBy());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Barang getBarangById(Long id) {
        logger.info("Fetching Barang with ID: {}", id); // Log saat mulai pencarian
    
        Optional<Barang> barangOptional = barangRepository.findById(id);
    
        if (barangOptional.isPresent()) {
            logger.info("Barang found with ID: {}, Details: {}", id, barangOptional.get()); // Log saat barang ditemukan
            return barangOptional.get();
        } else {
            logger.warn("Barang not found with ID: {}", id); // Log saat barang tidak ditemukan
            throw new ResourceNotFoundException("Barang not found with ID: " + id); // Exception handling
        }
    }

    @Override
    public void updateBarang(Long id, BarangUpdateDTO barangUpdateDTO, MultipartFile file) {
        try {
            logger.info("Attempting to update Barang with ID: {}", id); // Log saat mulai update
    
            Optional<Barang> barangOptional = barangRepository.findById(id);
            if (!barangOptional.isPresent()) {
                logger.warn("Barang not found with ID: {}", id); // Log jika barang tidak ditemukan
                throw new ResourceNotFoundException("Barang not found with ID: " + id);
            }
    
            Barang existingBarang = barangOptional.get(); // Ambil barang yang ada
            logger.info("Existing Barang before update: {}", existingBarang); // Log sebelum update
    
            String gambarBarang = existingBarang.getGambarBarang(); // Mengambil path gambar yang ada
    
            // Tangani unggahan file jika file baru disediakan
            if (file != null && !file.isEmpty()) {
                logger.info("Uploading new file for Barang ID: {}", id); // Log saat memulai unggah file
                Path filePath = FileUpload.uploadFile(file);
                gambarBarang = filePath.toString(); // Memperbarui path gambar
                logger.info("New image path for Barang ID {}: {}", id, gambarBarang); // Log gambar barang baru
            }
    
            // Konversi JSON string ke Map
            Map<String, Object> additionalInfoMap = objectMapper.readValue(barangUpdateDTO.getAdditionalInfo(), Map.class);
            String additionalInfo = objectMapper.writeValueAsString(additionalInfoMap); // Konversi kembali ke String untuk disimpan di jsonb
            logger.info("Additional info for Barang ID {}: {}", id, additionalInfo); // Log informasi tambahan
    
            // Memanggil metode pembaruan native
            barangRepository.updateBarangNative(id, 
                barangUpdateDTO.getNamaBarang(), 
                Integer.parseInt(barangUpdateDTO.getJumlahBarang()), 
                existingBarang.getNomorSeriBarang(), 
                additionalInfo, 
                gambarBarang, 
                LocalDateTime.now()
            );
    
            // Mengambil barang yang telah diperbarui
            Barang updatedBarang = barangRepository.findById(id).get();
            logger.info("Barang updated successfully with ID: {}, Details after update: {}", id, updatedBarang); // Log setelah update
    
        } catch (JsonProcessingException e) {
            logger.error("Error processing additionalInfo for Barang ID {}: {}", id, e.getMessage()); // Log error JSON
            throw new RuntimeException("Error processing additionalInfo: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error updating Barang with ID {}: {}", id, e.getMessage()); // Log error umum
            throw new RuntimeException("Error updating Barang: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteBarang(Long id) {
        logger.info("Attempting to delete Barang with ID: {}", id); // Log saat mulai proses penghapusan
        
        // Periksa apakah barang ada sebelum menghapus
        if (!barangRepository.existsById(id)) {
            logger.warn("Barang not found with ID: {}", id); // Log jika barang tidak ditemukan
            throw new ResourceNotFoundException("Barang not found with ID: " + id);
        }
        
        // Panggil metode penghapusan
        barangRepository.deleteById(id);
        
        logger.info("Barang deleted successfully with ID: {}", id); // Log setelah penghapusan berhasil
    }
}
