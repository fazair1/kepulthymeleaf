package com.juaracoding.kepulthymeleaf.dto.validation;

import com.juaracoding.kepulthymeleaf.dto.relation.RelAksesDTO;
import jakarta.validation.constraints.Pattern;

public class ValUserDTO {

    @Pattern(regexp = "^([a-z0-9\\.]{4,20})$",
            message = "Format Huruf kecil ,numeric dan titik saja min 4 max 20 karakter, contoh : fauzan.123")
    private String username;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+=\\-\\.]{8,}$",
            message = "Format minimal 1 angka, 1 huruf, min 8 karakter, contoh : aB412345")
    private String password;

    @Pattern(regexp = "^(?=.{1,256})(?=.{1,64}@.{1,255}$)(?:(?![.])[a-zA-Z0-9._%+-]+(?:(?<!\\\\)[.][a-zA-Z0-9-]+)*?)@[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,50})+$",
            message = "Format tidak valid contoh : user_name123@sub.domain.com")
    private String email;

    @Pattern(regexp = "^[a-zA-Z\\s]{4,50}$",message = "Hanya Alfabet dan spasi Minimal 4 Maksimal 50")
    private String nama;

    private RelAksesDTO akses;

    public RelAksesDTO getAkses() {
        return akses;
    }

    public void setAkses(RelAksesDTO akses) {
        this.akses = akses;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
