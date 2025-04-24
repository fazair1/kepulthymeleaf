package com.juaracoding.kepulthymeleaf.httpservice;

import com.juaracoding.kepulthymeleaf.config.FeignClientConfig;
import com.juaracoding.kepulthymeleaf.dto.validation.ValProductDTO;
import com.juaracoding.kepulthymeleaf.dto.validation.ValTransactionDTO;
import feign.Response;
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

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestHeader("Authorization") String token,
                                       @PathVariable(value = "id") Long id,
                                       @RequestBody ValTransactionDTO valTransactionDTO);

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@RequestHeader("Authorization") String token,
                                           @PathVariable(value = "id") Long id);

    @GetMapping("/{sort}/{sortBy}/{page}")
    public ResponseEntity<Object> findByParam(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "sort") String sort,
            @PathVariable(value = "sortBy") String sortBy,
            @PathVariable(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value);

    @GetMapping("/pdf")
    public Response downloadPdf(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value
    );

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@RequestHeader("Authorization") String token,
                                         @PathVariable(value = "id") Long id);

}
