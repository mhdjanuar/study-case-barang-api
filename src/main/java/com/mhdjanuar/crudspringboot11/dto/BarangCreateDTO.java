package com.mhdjanuar.crudspringboot11.dto;

import lombok.Data;

@Data
public class BarangCreateDTO {
    private String namaBarang;
    private String jumlahBarang;
    private String additionalInfo;
}
