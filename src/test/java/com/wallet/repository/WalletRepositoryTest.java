package com.wallet.repository;

import com.wallet.entity.Wallet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class WalletRepositoryTest {

    private static final String NAME = "Test Wallet";
    @Autowired
    WalletRepository repository;

    @Before
    public void setUp() {
        Wallet wallet = new Wallet();
        wallet.setName(NAME);
        wallet.setValue(BigDecimal.valueOf(500));

        repository.save(wallet);
    }

    @After
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    public void testSave() {
        Wallet wallet = new Wallet();
        wallet.setName(NAME);
        wallet.setValue(BigDecimal.valueOf(100.99));

        Wallet response = repository.save(wallet);
        assertNotNull(response);
    }

    @Test
    public void findByName() {
        PageRequest page = PageRequest.of(0, 10);
        Page<Wallet> response = repository.findByName(NAME.toLowerCase(), page);
        assertNotNull(response.getContent());
        assertEquals(response.getContent().size(), 1);
    }
}
