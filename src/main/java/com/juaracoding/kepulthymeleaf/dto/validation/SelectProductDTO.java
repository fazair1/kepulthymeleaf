package com.juaracoding.kepulthymeleaf.dto.validation;

import com.juaracoding.kepulthymeleaf.utils.ConstantsMessage;
import jakarta.validation.constraints.Pattern;

public class SelectProductDTO {

    private Long id;

    @Pattern(regexp = "^[\\w\\s]{3,50}$", message = ConstantsMessage.VAL_PRODUCT_CATEGORY_NAMA)
    private String nama;

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
}
