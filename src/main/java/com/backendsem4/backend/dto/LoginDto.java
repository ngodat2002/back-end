package com.backendsem4.backend.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String nameOrEmail;
    private String password;
}
