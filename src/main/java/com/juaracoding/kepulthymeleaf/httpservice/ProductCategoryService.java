package com.juaracoding.kepulthymeleaf.httpservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "product-category-services",url = "http://localhost:8081/product-category")
public interface ProductCategoryService {

    @GetMapping
    public ResponseEntity<Object> findAll();

}
