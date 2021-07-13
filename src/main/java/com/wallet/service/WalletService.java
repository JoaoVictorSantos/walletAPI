package com.wallet.service;

import com.wallet.entity.Wallet;
import org.springframework.data.domain.Page;

public interface WalletService {

    Wallet save(Wallet w);

    Page<Wallet> findByName(String name, int page);
}
