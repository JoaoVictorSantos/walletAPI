package com.wallet.controller;

import com.wallet.dto.WalletDTO;
import com.wallet.entity.Wallet;
import com.wallet.response.Response;
import com.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("wallet")
public class WalletController {

    @Autowired
    WalletService service;

    @PostMapping
    public ResponseEntity<Response<WalletDTO>> create(@Validated @RequestBody WalletDTO dto, BindingResult result) {
        Response<WalletDTO> response = new Response<WalletDTO>();
        if(result.hasErrors()){
            result.getAllErrors()
            .forEach(e -> response.getErrors().add(e.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Wallet w = service.save(convertDtoToEntity(dto));
        response.setData(convertEntityToDto(w));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private Wallet convertDtoToEntity(WalletDTO dto) {
        Wallet w = new Wallet();
        w.setId(dto.getId());
        w.setName(dto.getName());
        w.setValue(dto.getValue());

        return w;
    }

    private WalletDTO convertEntityToDto(Wallet w) {
        WalletDTO dto = new WalletDTO();
        dto.setId(w.getId());
        dto.setName(w.getName());
        dto.setValue(w.getValue());
        return dto;
    }
}
