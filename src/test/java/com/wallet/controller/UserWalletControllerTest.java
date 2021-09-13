package com.wallet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.dto.UserWalletDTO;
import com.wallet.entity.User;
import com.wallet.entity.UserWallet;
import com.wallet.entity.Wallet;
import com.wallet.service.UserWalletService;
import com.wallet.util.Util;
import com.wallet.util.enums.TypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserWalletControllerTest {

    private static final String URL = "/user-wallet";
    private static final Long ID = 1L;

    @Autowired
    MockMvc mvc;

    @MockBean
    UserWalletService service;

    @Test
    @WithMockUser
    public void testSave() throws Exception {
        BDDMockito.given(service.save(Mockito.any(UserWallet.class)))
            .willReturn(getMockUserWallet());

        mvc.perform(MockMvcRequestBuilders.post(URL)
            .content(getJsonPayload(ID, getUser().getId(), getWallet().getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.id").value(ID))
            .andExpect(jsonPath("$.data.users").value(getUser().getId()))
            .andExpect(jsonPath("$.data.wallet").value(getWallet().getId()));
    }

    @Test
    @WithMockUser
    public void testSaveInvalid() throws Exception {
        BDDMockito.given(service.save(Mockito.any(UserWallet.class)))
                .willReturn(getMockUserWallet());

        mvc.perform(MockMvcRequestBuilders.post(URL)
                .content(getJsonPayload(ID, null, getWallet().getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Informe o id do usuário."));
    }


    @Test
    @WithMockUser
    public void testFindWalletByUserId() throws Exception {
        List<Wallet> list = new ArrayList<>();
        list.add(getWallet());

        Page<Wallet> page = new PageImpl(list);

        BDDMockito.given(service.findWalletByUserId(Mockito.any(), Mockito.anyInt()))
                .willReturn(page);

        mvc.perform(MockMvcRequestBuilders.get(URL + "/by-user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(ID));
    }

    @Test
    @WithMockUser
    public void testDelete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Registro removido com sucesso!"));
    }

    @Test
    @WithMockUser
    public void testFindById() throws Exception {
        UserWallet uw = getMockUserWallet();
        uw.setId(ID);

        BDDMockito.given(service.findById(Mockito.anyLong()))
                .willReturn(Optional.of(getMockUserWallet()));

        mvc.perform(MockMvcRequestBuilders.get(URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(ID));
    }

    @Test
    @WithMockUser
    public void testUpdate() throws Exception {
        UserWallet uw = getMockUserWallet();
        uw.setId(ID);

        BDDMockito.given(service.findById(Mockito.anyLong()))
                .willReturn(Optional.of(getMockUserWallet()));
        BDDMockito.given(service.save(Mockito.any(UserWallet.class)))
                .willReturn(uw);

        mvc.perform(MockMvcRequestBuilders.put(URL + "/1")
                .content(getJsonPayload(ID, ID, 2l))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(ID))
                .andExpect(jsonPath("$.data.wallet").value(uw.getWallet().getId()));

    }

    private UserWallet getMockUserWallet() {
        UserWallet uw = new UserWallet();
        uw.setId(ID);
        uw.setUsers(getUser());
        uw.setWallet(getWallet());

        return uw;
    }

    private String getJsonPayload(Long id, Long user, Long wallet) throws JsonProcessingException {
        UserWalletDTO dto = new UserWalletDTO();
        dto.setId(id);
        dto.setUsers(user);
        dto.setWallet(wallet);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dto);
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
