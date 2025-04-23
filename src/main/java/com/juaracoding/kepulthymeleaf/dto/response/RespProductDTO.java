package com.juaracoding.kepulthymeleaf.dto.response;

import com.juaracoding.kepulthymeleaf.dto.relation.RelProductCategoryDTO;

public class RespProductDTO {

    private Long id;

    private String nama;

    private String deskripsi;

    private RelProductCategoryDTO productCategory;

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public RelProductCategoryDTO getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(RelProductCategoryDTO productCategory) {
        this.productCategory = productCategory;
    }
}
