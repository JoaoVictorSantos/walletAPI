package com.wallet.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private long id;
    @NotBlank
    @Length(min = 3, max = 50, message = "O Nome deve conter de 3 a 50 caracteres")
    private String name;
    @NotBlank
    @Length(min = 6, message = "O Senha deve conter pelo meno 6 caracteres")
    private String password;
    @Email(message = "Formato de Email inválido")
    private String email;

}
