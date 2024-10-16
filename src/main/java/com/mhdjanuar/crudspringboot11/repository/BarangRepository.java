package com.mhdjanuar.crudspringboot11.repository;

import com.mhdjanuar.crudspringboot11.domain.Barang;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BarangRepository extends JpaRepository<Barang, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO barang (id, nama_barang, jumlah_stok_barang, nomor_seri_barang, additional_info, gambar_barang, created_by, created_at) " +
                   "VALUES (nextval('barang_sequence'), :namaBarang, :jumlahStokBarang, :nomorSeriBarang, " +
                   "CAST(:additionalInfo AS jsonb), " +
                   ":gambarBarang, :createdBy, :createdAt)", nativeQuery = true)
    void saveNative(String namaBarang, Integer jumlahStokBarang, String nomorSeriBarang, String additionalInfo, String gambarBarang, String createdBy, LocalDateTime createdAt);   
    
    @Modifying
    @Transactional
    @Query(value = "UPDATE barang SET " +
                   "nama_barang = :namaBarang, " +
                   "jumlah_stok_barang = :jumlahStokBarang, " +
                   "nomor_seri_barang = :nomorSeriBarang, " +
                   "additional_info = CAST(:additionalInfo AS jsonb), " +
                   "gambar_barang = :gambarBarang, " +
                   "updated_at = :updatedAt " +
                   "WHERE id = :id", nativeQuery = true)
    void updateBarangNative(Long id, String namaBarang, Integer jumlahStokBarang, String nomorSeriBarang, String additionalInfo, String gambarBarang, LocalDateTime updatedAt);
}
