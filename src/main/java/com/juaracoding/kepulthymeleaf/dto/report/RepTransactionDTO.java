package com.juaracoding.kepulthymeleaf.dto.report;

import java.util.List;
import java.util.Map;

public class RepTransactionDTO {

    private Long id;

    private String namaDivisi;

    private String namaAdmin;

    private List<Map<String, Object>> ltProduct;

    private String namaStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Map<String, Object>> getLtProduct() {
        return ltProduct;
    }

    public void setLtProduct(List<Map<String, Object>> ltProduct) {
        this.ltProduct = ltProduct;
    }

    public String getNamaAdmin() {
        return namaAdmin;
    }

    public void setNamaAdmin(String namaAdmin) {
        this.namaAdmin = namaAdmin;
    }

    public String getNamaDivisi() {
        return namaDivisi;
    }

    public void setNamaDivisi(String namaDivisi) {
        this.namaDivisi = namaDivisi;
    }

    public String getNamaStatus() {
        return namaStatus;
    }

    public void setNamaStatus(String namaStatus) {
        this.namaStatus = namaStatus;
    }
}
