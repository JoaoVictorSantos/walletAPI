package com.wallet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.entity.Wallet;
import com.wallet.service.WalletService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class WalletControllerTest {

    private static final Long ID = 1L;
    private static final String NAME = "Test Wallet";
    private static final BigDecimal VALUE = BigDecimal.valueOf(100.99);
    private static final String URL = "/wallet";

    @Autowired
    MockMvc mvc;

    @MockBean
    WalletService service;

    @Test
    @WithMockUser
    public void testSave() throws Exception {
        BDDMockito.given(service.save(Mockito.any(Wallet.class)))
            .willReturn(getMockWallet());

        mvc.perform(MockMvcRequestBuilders.post(URL)
                .content(getJsonPayload(ID, NAME, VALUE))
                .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.id").value(ID))
        .andExpect(jsonPath("$.data.name").value(NAME))
        .andExpect(jsonPath("$.data.value").value(VALUE));
    }

    @Test
    @WithMockUser
    public void testSaveInvalid() throws Exception {
        BDDMockito.given(service.save(Mockito.any(Wallet.class)))
                .willReturn(getMockWallet());

        mvc.perform(MockMvcRequestBuilders.post(URL)
                .content(getJsonPayload(ID, "Te", VALUE))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("O Nome deve conter pelo meno 3 caracteres."));
    }

    @Test
    @WithMockUser
    public void testFindByName() throws Exception {
        List<Wallet> list = new ArrayList<>();
        list.add(getMockWallet());

        Page<Wallet> page = new PageImpl(list);

        BDDMockito.given(service.findByName(Mockito.anyString(), Mockito.anyInt()))
                .willReturn(page);

        mvc.perform(MockMvcRequestBuilders.get(URL + "?name=" + NAME)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(ID))
                .andExpect(jsonPath("$.data.content[0].name").value(NAME))
                .andExpect(jsonPath("$.data.content[0].value").value(VALUE));
    }

    public Wallet getMockWallet() {
        Wallet wallet = new Wallet();
        wallet.setId(ID);
        wallet.setName(NAME);
        wallet.setValue(VALUE);

        return wallet;
    }

    public String getJsonPayload(Long id, String name, BigDecimal value) throws JsonProcessingException {
        Wallet wallet = new Wallet();
        wallet.setId(id);
        wallet.setName(name);
        wallet.setValue(value);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(wallet);
    }
}
