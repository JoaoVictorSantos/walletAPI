package com.wallet.repository;

import com.wallet.entity.User;
import com.wallet.entity.UserWallet;
import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import com.wallet.util.enums.RoleEnum;
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
import java.util.List;
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
        UserWallet uw = getUserWallet();
        UserWallet response = repository.save(uw);

        assertNotNull(response);
    }

    @Test
    public void findWalletByUserId() {
        UserWallet uw = getUserWallet();

        repository.save(uw);
        PageRequest page = PageRequest.of(0, 10);
        Page<Wallet> response = repository.findWalletByUserId(1L, page);
        assertNotNull(response);
    }

    @Test
    public void testFindByUserIdAndWalletId(){
        UserWallet uw = getUserWallet();

        repository.save(uw);
        Optional<UserWallet> response = repository.findByUsersIdAndWalletId(ID, ID);
        assertTrue(response.isPresent());
        assertNotNull(response.get());
    }

    @Test
    public void testDelete() {
        UserWallet uw = getUserWallet();
        repository.save(uw);
        repository.deleteById(uw.getId());

        Optional<UserWallet> opt = repository.findById(uw.getId());
        assertFalse(opt.isPresent());
    }

    @Test
    public void testFindById() {
        UserWallet uw = getUserWallet();
        repository.save(uw);

        Optional<UserWallet> opt = repository.findById(uw.getId());

        assertTrue(opt.isPresent());
        assertEquals(uw.getId(), opt.get().getId());
    }

    public UserWallet getUserWallet() {
        UserWallet uw = new UserWallet();
        uw.setUsers(getUser());
        uw.setWallet(getWallet());

        return uw;
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
