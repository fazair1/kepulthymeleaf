package com.juaracoding.kepulthymeleaf.httpservice;

import com.juaracoding.kepulthymeleaf.dto.validation.ValProductCategoryDTO;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product-category-services",url = "http://localhost:8081/product-category")
public interface ProductCategoryService {

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader("Authorization") String token);

    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader("Authorization") String token,
                                       @RequestBody ValProductCategoryDTO valProductCategoryDTO);
    @GetMapping("/{sort}/{sortBy}/{page}")
    public ResponseEntity<Object> findByParam(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "sort") String sort,
            @PathVariable(value = "sortBy") String sortBy,
            @PathVariable(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value);

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@RequestHeader("Authorization") String token,
                                           @PathVariable(value = "id") Long id);

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestHeader("Authorization") String token,
                                       @PathVariable(value = "id") Long id,
                                       @RequestBody ValProductCategoryDTO valProductCategoryDTO);

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@RequestHeader("Authorization") String token,
                                         @PathVariable(value = "id") Long id);

    @GetMapping("/excel")
    public Response downloadExcel(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value
    );

    @GetMapping("/pdf")
    public Response downloadPdf(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value
    );

    @GetMapping("/all")
    public ResponseEntity<Object> allMenu(@RequestHeader("Authorization") String token);
}
