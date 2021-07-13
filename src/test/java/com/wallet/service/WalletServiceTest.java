package com.wallet.service;

import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import com.wallet.repository.WalletRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class WalletServiceTest {

    private static final String NAME = "Test Wallet";

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
        wallet.setName(NAME);
        wallet.setValue(BigDecimal.valueOf(100.99));

        Wallet response = service.save(wallet);
        assertNotNull(response);
    }

    @Test
    public void testFindByName() {
        List<Wallet> list = new ArrayList<>();
        list.add(getMockWallet());

        Page<Wallet> page = new PageImpl(list);

        BDDMockito.given(repository.findByName(Mockito.anyString(), Mockito.any(PageRequest.class)))
                .willReturn(page);

        Page<Wallet> response = service.findByName(NAME, 0);
        assertNotNull(response.getContent());
        assertEquals(response.getContent().size(), 1);
    }

    private Wallet getMockWallet() {
        Wallet w = new Wallet();
        w.setId(1L);

        return w;
    }
}
