package com.wallet.controller;

import com.wallet.dto.UserDTO;
import com.wallet.entity.User;
import com.wallet.response.Response;
import com.wallet.service.UserService;
import com.wallet.util.Bcrypt;
import com.wallet.util.Util;
import com.wallet.util.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService service;

    @PostMapping
    public ResponseEntity<Response<UserDTO>> create(@Validated @RequestBody UserDTO dto, BindingResult result) {
        Response<UserDTO> response = new Response<UserDTO>();
        if(result.hasErrors()){
            result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        User user = service.save(this.convertDtoToEntity(dto));
        response.setData(this.convertEntityToDto(user));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/logged-in")
    public ResponseEntity<Response<UserDTO>> LoggedIn() {
        Response<UserDTO> response = new Response<UserDTO>();
        Long id = Util.getAuthenticatedUserId();
        if(id == null){
            response.getErrors().add("Você precisa estar logado para acessar essa funcionalidade!");
            return ResponseEntity.badRequest().body(response);
        }
        UserDTO dto = new UserDTO();
        dto.setId(Util.getAuthenticatedUserId());
        response.setData(dto);
        return ResponseEntity.ok().body(response);
    }

    private User convertDtoToEntity(UserDTO dto){
        User u = new User();
        u.setId(dto.getId());
        u.setName(dto.getName());
        u.setEmail(dto.getEmail());
        u.setPassword(Bcrypt.getHash(dto.getPassword()));
        u.setRole(RoleEnum.valueOf(dto.getRole()));

        return u;
    }

    private UserDTO convertEntityToDto(User u){
        UserDTO dto = new UserDTO();
        dto.setId(u.getId());
        dto.setName(u.getName());
        dto.setEmail(u.getEmail());
        dto.setRole(u.getRole().toString());

        return dto;
    }
}
