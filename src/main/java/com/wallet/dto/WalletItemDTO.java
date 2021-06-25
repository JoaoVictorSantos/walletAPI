package com.wallet.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class WalletItemDTO {

    private Long id;

    @NotNull(message = "Informe o id da carteira.")
    private Long wallet;

    @NotNull(message = "Informe uma data.")
    private Date date;

    @NotNull(message = "Informe um tipo.")
    @Pattern(regexp = "^(ENTRADA|SAÍDA)$", message = "Para o Tipo somente são aceitOs ENTRADA ou SAÍDA")
    private String type;

    @NotNull(message = "Informe um tipo.")
    @Length(min = 5, message = "A descrição deve conter pelo meno 5 caracteres")
    private String description;

    @NotNull(message = "Informe um valor.")
    private BigDecimal value;
}
