package com.juaracoding.kepulthymeleaf.handler;

import com.juaracoding.kepulthymeleaf.config.OtherConfig;
import com.juaracoding.kepulthymeleaf.security.RequestCapture;
//import com.juaracoding.kepulthymeleaf.util.LoggingFile;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

/**
 *  Override proses exception / menjadi layer Validasi
 */

/**
 *  KODE GLOBAL = X
 *  Kode Exception
 *  VALIDATION = 01
 *  DATABASE / FILE / 3RD PARTY API = 02
 *  AUTH = 03
 *  MEDIA FORMAT / TYPE = 04
 *  OTHER = 05
 *
 */

@ControllerAdvice
public class GlobalExceptionHandling extends ResponseEntityExceptionHandler {

    private List<ApiSubError> apiSubErrors = new ArrayList<ApiSubError>();

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        apiSubErrors.clear();
        for(FieldError error : ex.getBindingResult().getFieldErrors()) {
            apiSubErrors.add(new ApiSubError(error.getField(),error.getDefaultMessage(),error.getRejectedValue()));
        }
//        LoggingFile.logException("GlobalExceptionHandler","handleMethodArgumentNotValid "+ RequestCapture.allRequest(request),ex, OtherConfig.getEnableLog());
        return new ResponseHandler().handleResponse("Data Tidak Valid", (HttpStatus) status,apiSubErrors,"X01001",request);
    }
    @Override
    protected ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        LoggingFile.logException("GlobalExceptionHandler","maxUploadSizeExceededException "+ RequestCapture.allRequest(request),ex, OtherConfig.getEnableLog());
        return new ResponseHandler().handleResponse("Ukuran File Melebihi Batas",HttpStatus.BAD_REQUEST,null,"X02001",request);
    }

    @ExceptionHandler(UnexpectedRollbackException.class)
    public ResponseEntity<Object> unexpectedRollbackException(UnexpectedRollbackException ex, HttpServletRequest request){
//        LoggingFile.logException("GlobalExceptionHandler","unexpectedRollbackException "+ RequestCapture.allRequest(request),ex, OtherConfig.getEnableLog());
        return new ResponseHandler().handleResponse("Rollback untuk Transaksi Telah dilakukan",HttpStatus.BAD_REQUEST,null,"X02002",request);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Object> multipartException(MultipartException ex, HttpServletRequest request){
//        LoggingFile.logException("GlobalExceptionHandler","multipartException "+ RequestCapture.allRequest(request),ex, OtherConfig.getEnableLog());
        return new ResponseHandler().handleResponse("Terjadi Kesalahan Di Server",HttpStatus.INTERNAL_SERVER_ERROR,null,"X04001",request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> runtimeException(RuntimeException ex, HttpServletRequest request){
//        LoggingFile.logException("GlobalExceptionHandler","runtimeException "+ RequestCapture.allRequest(request),ex, OtherConfig.getEnableLog());
        return new ResponseHandler().handleResponse("Terjadi Kesalahan Di Server",HttpStatus.INTERNAL_SERVER_ERROR,null,"X05001",request);
    }

    @ExceptionHandler(UncheckedIOException.class)
    public ResponseEntity<Object> uncheckedIOException(UncheckedIOException ex, HttpServletRequest request){
//        LoggingFile.logException("GlobalExceptionHandler","uncheckedIOException "+ RequestCapture.allRequest(request),ex, OtherConfig.getEnableLog());
        return new ResponseHandler().handleResponse("Terjadi Kesalahan Di Server",HttpStatus.INTERNAL_SERVER_ERROR,null,"X05002",request);
    }

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<Object> arithmeticException(ArithmeticException ex, HttpServletRequest request){
//        LoggingFile.logException("GlobalExceptionHandler","arithmeticException "+ RequestCapture.allRequest(request),ex, OtherConfig.getEnableLog());
        return new ResponseHandler().handleResponse("Proses Aritmatika / Kalkulasi Bermasalah",HttpStatus.INTERNAL_SERVER_ERROR,null,"X05003",request);
    }

//    @Override
//    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
//        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
//    }

    public ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, HttpServletRequest request) {
//        LoggingFile.logException("GlobalExceptionHandler","handleExceptionInternal "+ RequestCapture.allRequest(request),ex, OtherConfig.getEnableLog());
        return new ResponseHandler().handleResponse("Terjadi Kesalahan Di Server",status,null,"X05999",request);
    }

}
