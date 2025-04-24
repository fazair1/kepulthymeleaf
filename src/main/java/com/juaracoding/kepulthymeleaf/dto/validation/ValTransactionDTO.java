package com.juaracoding.kepulthymeleaf.dto.validation;

import com.juaracoding.kepulthymeleaf.dto.relation.RelProductDTO;
import com.juaracoding.kepulthymeleaf.dto.relation.RelStatusDTO;

import java.util.List;

public class ValTransactionDTO {

    private List<RelProductDTO> ltProduct;

    private RelStatusDTO status;

    public RelStatusDTO getStatus() {
        return status;
    }

    public void setStatus(RelStatusDTO status) {
        this.status = status;
    }

    public List<RelProductDTO> getLtProduct() {
        return ltProduct;
    }

    public void setLtProduct(List<RelProductDTO> ltProduct) {
        this.ltProduct = ltProduct;
    }
}
