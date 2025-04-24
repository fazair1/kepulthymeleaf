package com.juaracoding.kepulthymeleaf.httpservice;

import com.juaracoding.kepulthymeleaf.config.FeignClientConfig;
import com.juaracoding.kepulthymeleaf.dto.validation.ValProductDTO;
import com.juaracoding.kepulthymeleaf.dto.validation.ValTransactionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "transaction-service", url = "http://localhost:8081/transaction", configuration = FeignClientConfig.class)
public interface TransactionService {

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader("Authorization") String token);

    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader("Authorization") String token,
                                       @RequestBody ValTransactionDTO valTransactionDTO);

}
