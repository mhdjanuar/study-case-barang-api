package com.mhdjanuar.crudspringboot11.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "barang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Barang implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "barang_seq")
    @SequenceGenerator(name = "barang_seq", sequenceName = "barang_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "nama_barang", nullable = false)
    private String namaBarang;

    @Column(name = "jumlah_stok_barang", nullable = false)
    private Integer jumlahStokBarang;

    @Column(name = "nomor_seri_barang", nullable = false, unique = true)
    private String nomorSeriBarang;

    @Column(name = "additional_info", columnDefinition = "jsonb")
    private Map<String, Object> additionalInfo;

    @Column(name = "gambar_barang")
    private String gambarBarang;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }   

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
