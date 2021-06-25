package com.wallet.service.impl;

import com.wallet.entity.WalletItem;
import com.wallet.enums.TypeEnum;
import com.wallet.repository.WalletItemRepository;
import com.wallet.service.WalletItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class WalletItemServiceImpl implements WalletItemService {

    @Value("${pagination.items_ser_page}")
    private int itemsPerPage;

    @Autowired
    WalletItemRepository repository;

    @Override
    public WalletItem save(WalletItem wi) {
        return repository.save(wi);
    }

    @Override
    public Page<WalletItem> findBetweenDays(Long wallet, Date init, Date end, int page) {
        PageRequest pr = PageRequest.of(page, itemsPerPage);
        return repository.findAllByWalletIdAndDateGreaterThanEqualAndDateLessThanEqual(wallet, init, end, pr);
    }

    @Override
    public List<WalletItem> findByWalletAndType(Long wallet, TypeEnum type) {
        return repository.findByWalletIdAndType(wallet, type);
    }

    @Override
    public BigDecimal sumByWalletId(Long wallet) {
        return repository.sumByWallet(wallet);
    }
}
