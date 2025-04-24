package com.juaracoding.kepulthymeleaf.httpservice;

import com.juaracoding.kepulthymeleaf.config.FeignClientConfig;
import com.juaracoding.kepulthymeleaf.dto.validation.ValProductDTO;
import com.juaracoding.kepulthymeleaf.dto.validation.ValTransactionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "transaction-service", url = "http://localhost:8081/transaction", configuration = FeignClientConfig.class)
public interface TransactionService {

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader("Authorization") String token);

    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader("Authorization") String token,
                                       @RequestBody ValTransactionDTO valTransactionDTO);

    @GetMapping("/{sort}/{sortBy}/{page}")
    public ResponseEntity<Object> findByParam(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "sort") String sort,
            @PathVariable(value = "sortBy") String sortBy,
            @PathVariable(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value);

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@RequestHeader("Authorization") String token,
                                         @PathVariable(value = "id") Long id);

}
