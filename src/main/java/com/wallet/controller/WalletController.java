package com.wallet.controller;

import com.wallet.dto.WalletDTO;
import com.wallet.dto.WalletItemDTO;
import com.wallet.entity.UserWallet;
import com.wallet.entity.Wallet;
import com.wallet.response.Response;
import com.wallet.service.WalletService;
import com.wallet.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

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

    @GetMapping
    public ResponseEntity<Response<Page<WalletDTO>>> findByName(
            @RequestParam(name = "name") String name, @RequestParam(name = "page", defaultValue = "0") int page){
        Response<Page<WalletDTO>> response = new Response<>();
        Page<WalletDTO> items = service.findByName(name,page)
                .map(i -> convertEntityToDto(i));
        response.setData(items);
        return ResponseEntity.ok().body(response);
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
