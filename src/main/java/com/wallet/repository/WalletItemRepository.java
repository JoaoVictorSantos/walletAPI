package com.wallet.repository;

import com.wallet.entity.WalletItem;
import com.wallet.util.enums.TypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface WalletItemRepository extends JpaRepository<WalletItem, Long> {

    Page<WalletItem> findAllByWalletIdAndDateGreaterThanEqualAndDateLessThanEqual(Long wallet, Date init, Date end, Pageable page);

    List<WalletItem> findByWalletIdAndType(Long id, TypeEnum type);

    @Query(value = "Select sum(value) From WalletItem wi Where wi.wallet.id = :wallet")
    BigDecimal sumByWallet(@Param("wallet") Long wallet);
}
