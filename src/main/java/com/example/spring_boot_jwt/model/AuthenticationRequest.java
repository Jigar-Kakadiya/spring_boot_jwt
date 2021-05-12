package com.example.spring_boot_jwt.model;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String userName;
    private String password;

    public AuthenticationRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

}
