package com.wallet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.dto.UserDTO;
import com.wallet.entity.User;
import com.wallet.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    private static final Long ID = 1L;
    private static final String NAME = "Test Mock";
    private static final String EMAIL = "email@test.com";
    private static final String PASSWORD = "1234567";
    private static final String URL = "/user";

    @MockBean
    UserService service;

    @Autowired
    MockMvc mvc;

    @Test
    public void testSave() throws Exception {
        BDDMockito.given(service.save(Mockito.any(User.class))).willReturn(getMockUser());
        mvc.perform(MockMvcRequestBuilders.post(URL) // URL que está sendo chamada
                .content(getJsonPayLoad(ID, EMAIL, NAME, PASSWORD)) // Objeto json que está sendo passado na requisição
                .contentType(MediaType.APPLICATION_JSON) // Informando o tipo do dado
                .accept(MediaType.APPLICATION_JSON))// Informando o tipo do dado aceito
                .andExpect(status().isCreated())// Informando o tipo de status esperado
                .andExpect(jsonPath("$.data.id").value(ID))// Forma de verificar o valor de retorno
                .andExpect(jsonPath("$.data.email").value(EMAIL))
                .andExpect(jsonPath("$.data.name").value(NAME))
                .andExpect(jsonPath("$.data.password").doesNotExist());
    }

    @Test
    public void testSaveInvalid() throws Exception {
        BDDMockito.given(service.save(Mockito.any(User.class))).willReturn(getMockUser());
        mvc.perform(MockMvcRequestBuilders.post(URL) // URL que está sendo chamada
                .content(getJsonPayLoad(ID, "email", NAME, PASSWORD)) // Objeto json que está sendo passado na requisição
                .contentType(MediaType.APPLICATION_JSON) // Informando o tipo do dado
                .accept(MediaType.APPLICATION_JSON))// Informando o tipo do dado aceito
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Formato de Email inválido"));
    }

    public User getMockUser() {
        User u = new User();
        u.setId(ID);
        u.setEmail(EMAIL);
        u.setName(NAME);
        u.setPassword(PASSWORD);
        return u;
    }

    public String getJsonPayLoad(Long id, String email, String name, String password)
            throws JsonProcessingException {
        UserDTO dto = new UserDTO();
        dto.setId(id);
        dto.setEmail(email);
        dto.setName(name);
        dto.setPassword(password);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dto);
    }

}
