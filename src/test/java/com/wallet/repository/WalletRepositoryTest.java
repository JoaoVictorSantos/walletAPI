package com.wallet.repository;

import com.wallet.entity.Wallet;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class WalletRepositoryTest {

    @Autowired
    WalletRepository repository;

    @After
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    public void testSave() {
        Wallet wallet = new Wallet();
        wallet.setName("Test Wallet");
        wallet.setValue(BigDecimal.valueOf(100.99));

        Wallet response = repository.save(wallet);
        assertNotNull(response);
    }
}
