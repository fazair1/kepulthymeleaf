package com.juaracoding.kepulthymeleaf.httpservice;

import com.juaracoding.kepulthymeleaf.dto.validation.ValProductCategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "product-category-services",url = "http://localhost:8081/product-category")
public interface ProductCategoryService {

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader("Authorization") String token);

    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader("Authorization") String token,
                                       @RequestBody ValProductCategoryDTO valProductCategoryDTO);

}
