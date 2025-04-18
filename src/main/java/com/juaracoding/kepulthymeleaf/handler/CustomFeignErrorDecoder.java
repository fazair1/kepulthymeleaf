package com.juaracoding.kepulthymeleaf.handler;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

@Component
public class CustomFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> errorBody = objectMapper.readValue(response.body().asInputStream(), Map.class);

            // You can customize this to wrap as a specific exception
            return new FeignCustomException(
                    (String) errorBody.get("message"),
                    HttpStatus.valueOf(response.status()),
                    errorBody
            );

        } catch (IOException e) {
            return defaultDecoder.decode(methodKey, response);
        }
    }
}
