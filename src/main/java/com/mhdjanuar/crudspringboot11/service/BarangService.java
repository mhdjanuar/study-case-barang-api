package com.mhdjanuar.crudspringboot11.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mhdjanuar.crudspringboot11.domain.Barang;

public interface BarangService {
     public void createNewBarang(
          String namaBarang, 
          String jumlahBarang,
          String additionalInfo, 
          MultipartFile file
     );

     List<Barang> getAllBarang(); // New method to retrieve all Barang

     Barang getBarangById(Long id); // New method to retrieve Barang by ID

     void updateBarang(Long id, String namaBarang, String jumlahBarang, String additionalInfo, MultipartFile file); // New method

     void deleteBarang(Long id);
}
