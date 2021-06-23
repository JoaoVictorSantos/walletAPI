package com.wallet.service;

import static org.junit.Assert.assertNotNull;

import com.wallet.entity.Wallet;
import com.wallet.repository.WalletRepository;
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

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class WalletServiceTest {

    @Autowired
    WalletService service;

    @MockBean
    WalletRepository repository;

    @Before
    public void setUp() {
        BDDMockito.given(repository.save(Mockito.any(Wallet.class)))
            .willReturn(new Wallet());
    }

    @Test
    public void testSave() {
        Wallet wallet = new Wallet();
        wallet.setName("Test Wallet");
        wallet.setValue(BigDecimal.valueOf(100.99));

        Wallet response = service.save(wallet);
        assertNotNull(response);
    }
}
