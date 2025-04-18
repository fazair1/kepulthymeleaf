package com.juaracoding.kepulthymeleaf.dto.validation;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class ValRegisDTO {

    @Pattern(regexp = "^([a-z0-9\\.]{4,20})$",
            message = "Format Huruf kecil ,numeric dan titik saja min 4 max 20 karakter, contoh : fauzan.123")
    private String username;

    @NotEmpty(message = "Tidak Boleh Kosong !!")
    private String password;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "Format minimal 1 angka, 1 huruf, min 8 karakter, contoh : aB412345")
    private String confirmPassword;

    @Pattern(regexp = "^(?=.{1,256})(?=.{1,64}@.{1,255}$)(?:(?![.])[a-zA-Z0-9._%+-]+(?:(?<!\\\\)[.][a-zA-Z0-9-]+)*?)@[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,50})+$",
            message = "Format tidak valid contoh : user_name123@sub.domain.com")
    private String email;

    @Pattern(regexp = "^[a-zA-Z\\s]{4,50}$",message = "Hanya Alfabet dan spasi Minimal 4 Maksimal 50")
    private String nama;

    public  String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public String getAlamat() {
}
