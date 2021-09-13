package com.wallet.controller;

import com.wallet.dto.UserWalletDTO;
import com.wallet.dto.WalletDTO;
import com.wallet.entity.User;
import com.wallet.entity.UserWallet;
import com.wallet.entity.Wallet;
import com.wallet.response.Response;
import com.wallet.service.UserWalletService;
import com.wallet.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("user-wallet")
public class UserWalletController {

    private static final String ID_NULL = "Id não pode ser nulo.";

    @Autowired
    UserWalletService service;

    @PostMapping
    public ResponseEntity<Response<UserWalletDTO>> create(@Validated @RequestBody UserWalletDTO dto, BindingResult result) {
        Response<UserWalletDTO> response = new Response<UserWalletDTO>();
        if(result.hasErrors()){
            result.getAllErrors()
                    .forEach(e -> response.getErrors().add(e.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        UserWallet uw = service.save(convertDtoToEntity(dto));
        response.setData(convertEntityToDto(uw));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/by-user")
    public ResponseEntity<Response<Page<WalletDTO>>> findWalletByUserId(@RequestParam(name = "page", defaultValue = "0") int page){
        Response<Page<WalletDTO>> response = new Response<>();

        Page<WalletDTO> items = service.findWalletByUserId(Util.getAuthenticatedUserId(), page)
                .map(w -> {
                    WalletDTO dto = new WalletDTO();
                    dto.setId(w.getId());
                    dto.setName(w.getName());
                    dto.setValue(w.getValue());
                    return dto;
                });
        response.setData(items);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<UserWalletDTO>> findById(@PathVariable("id") Long id){
        Response<UserWalletDTO> response = new Response<UserWalletDTO>();

        if(id != null){
            Optional<UserWallet> opt = service.findById(id);
            if(!opt.isPresent()){
                response.getErrors().add("Registro não encontrado!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            response.setData(convertEntityToDto(opt.get()));
            return ResponseEntity.ok().body(response);
        } else {
            response.getErrors().add(ID_NULL);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<UserWalletDTO>> update(@PathVariable("id") Long id,
                                                          @Validated @RequestBody UserWalletDTO dto,
                                                          BindingResult result) {
        Response<UserWalletDTO> response = new Response<UserWalletDTO>();
        if(result.hasErrors()){
            result.getAllErrors()
                    .forEach(e -> response.getErrors().add(e.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if(id == null){
            response.getErrors().add(ID_NULL);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Optional<UserWallet> opt = service.findById(id);
        if(!opt.isPresent()){
            response.getErrors().add("Registro não encontrado!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        UserWallet uw = service.save(convertDtoToEntity(dto));
        response.setData(convertEntityToDto(uw));
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> deleteById(@PathVariable("id") Long id) {
        Response<String> response = new Response<>();
        if(id == null){
            response.getErrors().add(ID_NULL);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        service.deleteById(id);
        response.setData("Registro removido com sucesso!");
        return ResponseEntity.ok().body(response);
    }

    private UserWallet convertDtoToEntity(UserWalletDTO dto) {
        UserWallet uw = new UserWallet();

        User u = new User();
        u.setId(dto.getUsers());

        Wallet w = new Wallet();
        w.setId(dto.getWallet());

        uw.setId(dto.getId());
        uw.setUsers(u);
        uw.setWallet(w);

        return uw;
    }

    private UserWalletDTO convertEntityToDto(UserWallet uw) {
        UserWalletDTO dto = new UserWalletDTO();
        dto.setId(uw.getId());
        dto.setUsers(uw.getUsers().getId());
        dto.setWallet(uw.getWallet().getId());

        return dto;
    }
}
