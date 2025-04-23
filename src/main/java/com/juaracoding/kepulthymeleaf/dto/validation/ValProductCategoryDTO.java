package com.juaracoding.kepulthymeleaf.dto.validation;

import com.juaracoding.kepulthymeleaf.utils.ConstantsMessage;
import jakarta.validation.constraints.Pattern;

public class ValProductCategoryDTO {

    @Pattern(regexp = "^[\\w\\s]{3,50}$", message = ConstantsMessage.VAL_PRODUCT_CATEGORY_NAMA)
    private String nama;
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
