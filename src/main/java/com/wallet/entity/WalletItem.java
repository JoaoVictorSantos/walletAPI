package com.wallet.entity;

import com.wallet.util.enums.TypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Table(name="wallet_items")
@NoArgsConstructor
@AllArgsConstructor
public class WalletItem implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6247446483779558189L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "wallet", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Wallet wallet;

    @NotNull
    private Date date;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TypeEnum type;

    @NotNull
    private String description;

    @NotNull
    private BigDecimal value;
}
