package com.wallet.service;

import com.wallet.entity.User;
import com.wallet.entity.UserWallet;
import com.wallet.entity.Wallet;
import com.wallet.repository.UserWalletRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserWalletServiceTest {

    private static final Long ID = 1L;

    @MockBean
    UserWalletRepository repository;

    @Autowired
    UserWalletService service;

    @Before
    public void setUp() {
        BDDMockito.given(repository.save(Mockito.any(UserWallet.class)))
            .willReturn(new UserWallet());
    }

    @Test
    public void testSave(){
        UserWallet uw = new UserWallet();
        uw.setId(ID);
        uw.setUsers(getUser());
        uw.setWallet(getWallet());

        UserWallet response = service.save(uw);
        assertNotNull(response);
    }

    private User getUser(){
        User u = new User();
        u.setId(ID);

        return u;
    }

    private Wallet getWallet() {
        Wallet w = new Wallet();
        w.setId(ID);
        return w;
    }
}
