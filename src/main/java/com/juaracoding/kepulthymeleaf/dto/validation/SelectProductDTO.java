package com.juaracoding.kepulthymeleaf.dto.validation;

import com.juaracoding.kepulthymeleaf.utils.ConstantsMessage;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class SelectProductDTO {

    private Long id;

    @NotEmpty
    @NotNull
    @Pattern(regexp = "^[\\w\\s]{3,50}$", message = ConstantsMessage.VAL_PRODUCT_CATEGORY_NAMA)
    private String nama;

    @NotEmpty
    @NotNull
    @Pattern(regexp = "^[\\w\\s]{5,100}$",message = "Alfanumerik dengan spasi min 5 maks 100 karakter")
    private String deskripsi;

    private Long productCategory;

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

    public String getDeskripsi() {
        return deskripsi;
    }

    public Long getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Long productCategory) {
        this.productCategory = productCategory;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }


}
