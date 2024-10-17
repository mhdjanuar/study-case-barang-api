package com.mhdjanuar.crudspringboot11.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mhdjanuar.crudspringboot11.domain.Barang;
import com.mhdjanuar.crudspringboot11.dto.BarangCreateDTO;
import com.mhdjanuar.crudspringboot11.dto.BarangResponseDTO;
import com.mhdjanuar.crudspringboot11.dto.BarangUpdateDTO;

public interface BarangService {
     public void createNewBarang(
          BarangCreateDTO barang,
          MultipartFile file
     );

     List<BarangResponseDTO> getAllBarang(); // New method to retrieve all Barang

     Barang getBarangById(Long id); // New method to retrieve Barang by ID

     void updateBarang(Long id, BarangUpdateDTO barang, MultipartFile file); 

     void deleteBarang(Long id);
}
