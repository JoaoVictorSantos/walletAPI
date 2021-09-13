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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class
UserWalletServiceTest {

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

    @Test
    public void testFindByUserIdAndWalletId() {
        UserWallet uw = new UserWallet();
        uw.setUsers(getUser());
        uw.setWallet(getWallet());

        BDDMockito.given(repository.findByUsersIdAndWalletId(Mockito.anyLong(), Mockito.anyLong()))
        .willReturn(Optional.of(uw));

        Optional<UserWallet> response = service
            .findByUsersIdAndWalletId(uw.getUsers().getId(), uw.getWallet().getId());

        assertTrue(response.isPresent());
        assertNotNull(response.get());
        assertEquals(response.get().getUsers().getId(), uw.getUsers().getId());
        assertEquals(response.get().getWallet().getId(), uw.getWallet().getId());
    }

    @Test
    public void testFindWalletByUserId() {
        List<Wallet> list = new ArrayList<>();
        list.add(getWallet());

        Page<Wallet> page = new PageImpl(list);

        BDDMockito.given(repository.findWalletByUserId(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .willReturn(page);

        Page<Wallet> response = service.findWalletByUserId(ID, 0);
        assertNotNull(response.getContent());
        assertEquals(response.getContent().size(), 1);
    }

    @Test
    public void testFindById() {
        BDDMockito.given(repository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(getUserWallet()));

        Optional<UserWallet> response = repository.findById(ID);
        assertTrue(response.isPresent());
        assertEquals(getUserWallet().getUsers().getId(), response.get().getUsers().getId());
    }

    @Test
    public void testDelete() {
        BDDMockito.given(repository.findById(Mockito.anyLong()))
                .willReturn(Optional.empty());

        service.deleteById(ID);
        Optional<UserWallet> response = repository.findById(ID);

        assertFalse(response.isPresent());
    }

    private UserWallet getUserWallet() {
        UserWallet uw = new UserWallet();
        uw.setUsers(getUser());
        uw.setWallet(getWallet());

        return uw;
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
