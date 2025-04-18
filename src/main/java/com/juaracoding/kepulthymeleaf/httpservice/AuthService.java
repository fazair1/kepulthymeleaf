package com.juaracoding.kepulthymeleaf.httpservice;

import com.juaracoding.kepulthymeleaf.dto.validation.ValLoginDTO;
import com.juaracoding.kepulthymeleaf.dto.validation.ValRegisDTO;
import com.juaracoding.kepulthymeleaf.dto.validation.ValVerifyOTPRegisDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", url = "http://localhost:8081/auth")
public interface AuthService {

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody ValLoginDTO valLoginDTO);

    @PostMapping("/regis")
    public ResponseEntity<Object> regis(@RequestBody ValRegisDTO valRegisDTO);

    @PostMapping("/verify-regis")
    public ResponseEntity<Object> verifyRegis(@RequestBody ValVerifyOTPRegisDTO valVerifyOTPRegisDTO);

}
