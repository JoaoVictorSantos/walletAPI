package com.wallet.service;

import com.wallet.entity.WalletItem;
import com.wallet.enums.TypeEnum;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface WalletItemService {

    WalletItem save(WalletItem wi);

    Page<WalletItem> findBetweenDays(Long wallet, Date init, Date end, int page);

    List<WalletItem> findByWalletAndType(Long wallet, TypeEnum type);

    BigDecimal sumByWalletId(Long wallet);

    Optional<WalletItem> findById(Long id);

    void deleteById(Long id);
}
