package com.wallet.service;

import com.wallet.entity.UserWallet;
import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserWalletService {

    UserWallet save(UserWallet uw);

    Optional<UserWallet> findByUsersIdAndWalletId(Long user, Long wallet);

    Page<Wallet> findWalletByUserId(Long user, int page);

    Optional<UserWallet> findById(Long id);

    void deleteById(Long id);
}
