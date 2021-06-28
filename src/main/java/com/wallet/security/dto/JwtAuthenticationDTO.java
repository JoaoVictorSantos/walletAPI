package com.wallet.security.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

@SuppressWarnings("deprecation")
@Data
public class JwtAuthenticationDTO {

    @NotNull(message = "Informe um email")
    @NotEmpty(message = "Informe um email")
    @Email(message = "Formato de Email inválido")
    private String email;
    @NotNull(message = "Informe uma senha")
    @NotEmpty(message = "Informe uma senha")
    private String password;

}