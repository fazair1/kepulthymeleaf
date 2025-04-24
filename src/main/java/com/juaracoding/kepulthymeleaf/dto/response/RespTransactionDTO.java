package com.juaracoding.kepulthymeleaf.dto.response;

import com.juaracoding.kepulthymeleaf.dto.relation.RelProductDTO;
import com.juaracoding.kepulthymeleaf.dto.relation.RelStatusDTO;
import com.juaracoding.kepulthymeleaf.dto.relation.RelUserDTO;

import java.util.List;

public class RespTransactionDTO {

    private Long id;

    private RelUserDTO divisionId;

    private RelUserDTO adminId;

    private List<RelProductDTO> ltProduct;

    private RelStatusDTO status;

    public RelStatusDTO getStatus() {
        return status;
    }

    public void setStatus(RelStatusDTO status) {
        this.status = status;
    }

    public RelUserDTO getAdminId() {
        return adminId;
    }

    public void setAdminId(RelUserDTO adminId) {
        this.adminId = adminId;
    }

    public RelUserDTO getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(RelUserDTO divisionId) {
        this.divisionId = divisionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<RelProductDTO> getLtProduct() {
        return ltProduct;
    }

    public void setLtProduct(List<RelProductDTO> ltProduct) {
        this.ltProduct = ltProduct;
    }
}
