package com.juaracoding.kepulthymeleaf.dto.validation;


import com.juaracoding.kepulthymeleaf.dto.relation.RelProductCategoryDTO;
import com.juaracoding.kepulthymeleaf.utils.ConstantsMessage;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class ValProductDTO {

    @Pattern(regexp = "^[\\w\\s]{3,50}$", message = ConstantsMessage.VAL_PRODUCT_CATEGORY_NAMA)
    private String nama;

    @Pattern(regexp = "^[\\w\\s]{3,100}$", message = ConstantsMessage.VAL_PRODUCT_DESKRIPSI)
    private String deskripsi;

    @NotNull(message = ConstantsMessage.VAL_PRODUCT_CATEGORY)
    private RelProductCategoryDTO productCategory;

    public RelProductCategoryDTO getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(RelProductCategoryDTO productCategory) {
        this.productCategory = productCategory;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

}
