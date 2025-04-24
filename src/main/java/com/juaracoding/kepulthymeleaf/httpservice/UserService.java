package com.juaracoding.kepulthymeleaf.httpservice;

import com.juaracoding.kepulthymeleaf.config.FeignClientConfig;
import com.juaracoding.kepulthymeleaf.dto.validation.ValEditUserDTO;
import com.juaracoding.kepulthymeleaf.dto.validation.ValTransactionDTO;
import com.juaracoding.kepulthymeleaf.dto.validation.ValUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "http://localhost:8081/user", configuration = FeignClientConfig.class)
public interface UserService {

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader("Authorization") String token);

    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader("Authorization") String token,
                                       @RequestBody ValUserDTO valUserDTO);

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestHeader("Authorization") String token,
                                       @PathVariable(value = "id") Long id,
                                       @RequestBody ValEditUserDTO valEditUserDTO);

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@RequestHeader("Authorization") String token,
                                         @PathVariable(value = "id") Long id);

}
