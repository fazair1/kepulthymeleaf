package com.juaracoding.kepulthymeleaf.httpservice;

import com.juaracoding.kepulthymeleaf.dto.validation.ValProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product-services",url = "http://localhost:8081/product")
public interface ProductService {

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader("Authorization") String token);

    @GetMapping("/{sort}/{sortBy}/{page}")
    public ResponseEntity<Object> findByParam(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "sort") String sort,
            @PathVariable(value = "sortBy") String sortBy,
            @PathVariable(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value);

    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader("Authorization") String token,
                                       @RequestBody ValProductDTO valProductDTO);

}
