package com.wallet.repository;

import com.wallet.entity.UserWallet;
import com.wallet.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {

    Optional<UserWallet> findByUsersIdAndWalletId(Long user, Long wallet);

    @Query(value = " Select w From UserWallet uw " +
            " Inner join Wallet w on uw.wallet = w.id" +
            " where uw.users.id = :users",
    countQuery = "SELECT count(distinct(w.id)) From UserWallet uw " +
            " Inner join Wallet w on uw.wallet = w.id" +
            " where uw.users.id = :users")
    Page<Wallet> findWalletByUserId(@Param("users") Long users, Pageable pageable);
}
