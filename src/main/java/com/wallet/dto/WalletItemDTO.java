package com.wallet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
    private Date date;

    @NotNull(message = "Informe um tipo.")
    @Pattern(regexp = "^(ENTRADA|SAÍDA)$", message = "Para o Tipo somente são aceitos ENTRADA ou SAÍDA.")
    private String type;

    @NotNull(message = "Informe um tipo.")
    @Length(min = 5, message = "A descrição deve conter pelo meno 5 caracteres.")
    private String description;

    @NotNull(message = "Informe um valor.")
    private BigDecimal value;
}
