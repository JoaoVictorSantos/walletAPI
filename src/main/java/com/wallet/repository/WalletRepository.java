package com.wallet.repository;

import com.wallet.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Query(value = "Select w From Wallet w Where (:name is not null And Lower(w.name) Like %:name%)",
            countQuery = "SELECT count(*) From Wallet w Where (:name is not null And Lower(w.name) Like %:name%)")
    Page<Wallet> findByName(@Param("name") String name, Pageable pageable);
}
