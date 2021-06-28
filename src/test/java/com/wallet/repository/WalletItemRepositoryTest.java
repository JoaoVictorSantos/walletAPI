package com.wallet.repository;

import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import com.wallet.util.enums.TypeEnum;
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

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class WalletItemRepositoryTest {

    private static final Date DATE = new Date();
    private static final TypeEnum TYPE = TypeEnum.EN;
    private static final String DESCRIPTION = "Conta de Luz";
    private static final BigDecimal VALUE = BigDecimal.valueOf(65);
    private Long saveWalletItemId = null;
    private Long saveWalletId = null;

    @Autowired
    WalletItemRepository repository;

    @Autowired
    WalletRepository walletRepository;

    @Before
    public void setUp() {
        Wallet w = new Wallet();
        w.setName("Carteira Teste");
        w.setValue(BigDecimal.valueOf(400));
        walletRepository.save(w);

        WalletItem wi = new WalletItem(null, w, DATE, TYPE, DESCRIPTION, VALUE);
        wi.setWallet(w);
        WalletItem response = repository.save(wi);

        saveWalletId = w.getId();
        saveWalletItemId = wi.getId();
    }

    @After
    public void tearDown() {
        repository.deleteAll();
        walletRepository.deleteAll();
    }

    @Test
    public void testSave() {
        Wallet w = new Wallet();
        w.setName("Carteira 1");
        w.setValue(BigDecimal.valueOf(500));
        walletRepository.save(w);

        WalletItem wi = new WalletItem(1L, w, DATE, TYPE, DESCRIPTION, VALUE);
        WalletItem response = repository.save(wi);

        assertNotNull(response);
        assertEquals(response.getDescription(), DESCRIPTION);
        assertEquals(response.getWallet().getId(), w.getId());
        assertEquals(response.getType(), TYPE);
        assertEquals(response.getValue(), VALUE);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSaveInvalid() {
        WalletItem wi = new WalletItem(null, null, null, null, DESCRIPTION, VALUE);
        repository.save(wi);
    }

    @Test
    public void testUpdate() {
        String description = "Nova descrição";
        Optional<WalletItem> opt = repository.findById(saveWalletItemId);
        WalletItem change = opt.get();
        change.setDescription(description);

        repository.save(change);

        Optional<WalletItem> optChange = repository.findById(saveWalletItemId);

        assertEquals(optChange.get().getDescription(), description);
    }

    @Test
    public void testDelete() {
        Optional<Wallet> optionalWallet = walletRepository.findById(saveWalletId);

        WalletItem wi = new WalletItem(null, optionalWallet.get(), DATE, TYPE, DESCRIPTION, VALUE);
        repository.save(wi);
        repository.deleteById(wi.getId());

        Optional<WalletItem> opt = repository.findById(wi.getId());
        assertFalse(opt.isPresent());
    }

    @Test
    public void testFindByBetweenDays() {
        Optional<Wallet> opt = walletRepository.findById(saveWalletId);

        LocalDateTime localDateTime = DATE.toInstant().atZone(ZoneId.systemDefault())
            .toLocalDateTime();

        Date currentDatePlusFiveDays = Date.from(localDateTime.plusDays(5)
            .atZone(ZoneId.systemDefault()).toInstant());
        Date currentDatePlusSevenDays = Date.from(localDateTime.plusDays(7)
            .atZone(ZoneId.systemDefault()).toInstant());

        repository.save(new WalletItem(null, opt.get(), currentDatePlusFiveDays,
            TypeEnum.SD, DESCRIPTION, VALUE));
        repository.save(new WalletItem(null, opt.get(), currentDatePlusSevenDays,
            TypeEnum.SD, DESCRIPTION, VALUE));

        PageRequest page = PageRequest.of(0, 10);
        Page<WalletItem> response = repository
            .findAllByWalletIdAndDateGreaterThanEqualAndDateLessThanEqual(saveWalletId, DATE, currentDatePlusFiveDays, page);

        assertEquals(response.getContent().size(), 2);
        assertEquals(response.getTotalElements(), 2);
        assertEquals(response.getContent().get(0).getWallet().getId(), saveWalletId);
    }

    @Test
    public void testFindByType() {
        List<WalletItem> response = repository.findByWalletIdAndType(saveWalletId, TYPE);
        assertEquals(response.size(), 1);
        assertEquals(response.get(0).getType(), TYPE);
    }

    @Test
    public void testFindByTypeSD() {
        Optional<Wallet> optionalWallet = walletRepository.findById(saveWalletId);
        repository.save(new WalletItem(null, optionalWallet.get(), DATE, TypeEnum.SD, DESCRIPTION, VALUE));

        List<WalletItem> response = repository.findByWalletIdAndType(saveWalletId,  TypeEnum.SD);
        assertEquals(response.size(), 1);
        assertEquals(response.get(0).getType(), TypeEnum.SD);
    }

    @Test
    public void testSumByWallet() {
        Optional<Wallet> w = walletRepository.findById(saveWalletId);
        repository.save(new WalletItem(null, w.get(), DATE, TypeEnum.SD, DESCRIPTION, BigDecimal.valueOf(100)));

        BigDecimal response = repository.sumByWallet(saveWalletId);
        assertEquals(response.compareTo(BigDecimal.valueOf(165)), 0);
    }
}
