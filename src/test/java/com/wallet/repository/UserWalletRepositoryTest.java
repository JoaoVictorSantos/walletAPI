package com.wallet.repository;

import com.wallet.entity.User;
import com.wallet.entity.UserWallet;
import com.wallet.entity.Wallet;
import com.wallet.util.enums.RoleEnum;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserWalletRepositoryTest {

    private static final Long ID = 1L;
    private static final String NAME = "Test User Wallet";

    @Autowired
    private UserWalletRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  WalletRepository walletRepository;

    @Before
    public void setUp() {
        userRepository.save(getUser());
        walletRepository.save(getWallet());
    }

    @Test
    public void testSave() {
        UserWallet uw = new UserWallet();
        uw.setUsers(getUser());
        uw.setWallet(getWallet());

        UserWallet response = repository.save(uw);
        assertNotNull(response);
    }

    @Test
    public void testFindByUserIdAndWalletId(){
        UserWallet uw = new UserWallet();
        uw.setUsers(getUser());
        uw.setWallet(getWallet());

        repository.save(uw);
        Optional<UserWallet> response = repository.findByUsersIdAndWalletId(ID, ID);
        assertTrue(response.isPresent());
        assertNotNull(response.get());
    }

    private User getUser(){
        User u = new User();
        u.setId(ID);
        u.setName(NAME);
        u.setEmail("email@teste.com");
        u.setPassword("123456");
        u.setRole(RoleEnum.ROLE_ADMIN);

        return u;
    }

    private Wallet getWallet() {
        Wallet w = new Wallet();
        w.setId(ID);
        w.setName(NAME);
        w.setValue(BigDecimal.valueOf(100.09));

        return w;
    }
}
