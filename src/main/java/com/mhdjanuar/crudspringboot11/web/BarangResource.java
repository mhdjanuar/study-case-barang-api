package com.mhdjanuar.crudspringboot11.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.mhdjanuar.crudspringboot11.domain.Barang;
import com.mhdjanuar.crudspringboot11.service.BarangService;

@RestController
public class BarangResource {

    @Autowired
    private BarangService barangService;

    @PostMapping(value = "/barang", consumes = "multipart/form-data")
    public ResponseEntity<String> createNewBook(
        @RequestPart("namaBarang") String namaBarang,
        @RequestPart("jumlahBarang") String jumlahBarang,
        @RequestPart("additionalInfo") String additionalInfo,
        @RequestPart("file") MultipartFile file
    ) {
        barangService.createNewBarang(namaBarang, jumlahBarang, additionalInfo, file);

        return ResponseEntity.ok("Barang created successfully");
    }

    @GetMapping(value = "/barang")
    public ResponseEntity<List<Barang>> getAllBarang() {
        List<Barang> barangList = barangService.getAllBarang();
        return ResponseEntity.ok(barangList);
    }

    @GetMapping(value = "/barang/{id}")
    public ResponseEntity<Barang> getBarangById(@PathVariable Long id) {
        Barang barang = barangService.getBarangById(id);
        return ResponseEntity.ok(barang);
    }

    @PutMapping(value = "/barang/{id}", consumes = "multipart/form-data")
    public ResponseEntity<String> updateBarang(
        @PathVariable Long id,
        @RequestPart("namaBarang") String namaBarang,
        @RequestPart("jumlahBarang") String jumlahBarang,
        @RequestPart(value = "additionalInfo", required = false) String additionalInfo,
        @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        barangService.updateBarang(id, namaBarang, jumlahBarang, additionalInfo, file);
        return ResponseEntity.ok("Barang updated successfully");
    }

    @DeleteMapping("/barang/{id}")
    public ResponseEntity<String> deleteBarang(@PathVariable Long id) {
        barangService.deleteBarang(id);
        return ResponseEntity.ok("Barang deleted successfully");
    }
}
