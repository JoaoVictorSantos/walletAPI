package com.wallet.service.impl;

import com.wallet.entity.UserWallet;
import com.wallet.entity.Wallet;
import com.wallet.repository.UserWalletRepository;
import com.wallet.service.UserWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserWalletServiceImpl implements UserWalletService {

    @Autowired
    private UserWalletRepository repository;

    @Value("${pagination.items_ser_page}")
    private int itemsPerPage;

    @Override
    public UserWallet save(UserWallet uw) {
        return repository.save(uw);
    }

    @Override
    public Optional<UserWallet> findByUsersIdAndWalletId(Long user, Long wallet) {
        return repository.findByUsersIdAndWalletId(user, wallet);
    }

    @Override
    public Page<Wallet> findWalletByUserId(Long user, int page) {
        PageRequest pr = PageRequest.of(page, itemsPerPage);
        return repository.findWalletByUserId(user, pr);
    }

    @Override
    public Optional<UserWallet> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
