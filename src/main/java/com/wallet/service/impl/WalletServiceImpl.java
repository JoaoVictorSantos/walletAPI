package com.wallet.service.impl;

import com.wallet.entity.Wallet;
import com.wallet.repository.WalletRepository;
import com.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService {

    @Value("${pagination.items_ser_page}")
    private int itemsPerPage;

    @Autowired
    WalletRepository repository;

    @Override
    public Wallet save(Wallet w) {
        return repository.save(w);
    }

    @Override
    public Page<Wallet> findByName(String name, int page) {
        PageRequest pr = PageRequest.of(page, itemsPerPage);
        if(name == null || name.equals("null")){
            return repository.findAll(pr);
        }
        return repository.findByName(name.toLowerCase(), pr);
    }
}