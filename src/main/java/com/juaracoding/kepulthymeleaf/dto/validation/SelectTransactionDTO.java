package com.juaracoding.kepulthymeleaf.dto.validation;

import com.juaracoding.kepulthymeleaf.dto.relation.RelProductDTO;
import com.juaracoding.kepulthymeleaf.dto.relation.RelStatusDTO;

import java.util.List;

public class SelectTransactionDTO {

    private List<String> ltProduct;

    private RelStatusDTO status;

    public List<String> getLtProduct() {
        return ltProduct;
    }

    public void setLtProduct(List<String> ltProduct) {
        this.ltProduct = ltProduct;
    }

    public RelStatusDTO getStatus() {
        return status;
    }

    public void setStatus(RelStatusDTO status) {
        this.status = status;
    }
}
