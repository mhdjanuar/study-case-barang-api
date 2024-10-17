package com.mhdjanuar.crudspringboot11.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BarangUpdateDTO {
    private String namaBarang;
    private String jumlahBarang;
    private String additionalInfo;
}
