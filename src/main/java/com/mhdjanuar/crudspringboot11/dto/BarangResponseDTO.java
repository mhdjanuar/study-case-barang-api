package com.mhdjanuar.crudspringboot11.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BarangResponseDTO {

    private Long id;
    private String namaBarang;
    private Integer jumlahStokBarang;
    private String nomorSeriBarang;
    private Map<String, Object> additionalInfo;  // Menyimpan data JSON
    private String gambarBarang;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

}
