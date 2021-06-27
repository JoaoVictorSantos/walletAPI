package com.wallet.controller;

import com.wallet.dto.WalletItemDTO;
import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import com.wallet.enums.TypeEnum;
import com.wallet.response.Response;
import com.wallet.service.WalletItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("wallet-item")
public class WalletItemController {

    @Autowired
    WalletItemService service;

    @PostMapping
    public ResponseEntity<Response<WalletItemDTO>> create(@Validated @RequestBody WalletItemDTO dto,
                                                          BindingResult result) {
        Response<WalletItemDTO> response = new Response<>();
        if (result.hasErrors()) {
            result.getAllErrors()
                    .forEach(e -> response.getErrors().add(e.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        WalletItem wi = service.save(convertDtoToEntity(dto));
        response.setData(convertEntityToDto(wi));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private WalletItem convertDtoToEntity(WalletItemDTO dto) {
        Wallet w = new Wallet();
        w.setId(dto.getWallet());

        WalletItem wi = new WalletItem(null, w, dto.getDate(),
                TypeEnum.getEnum(dto.getType()), dto.getDescription(), dto.getValue());
        return wi;
    }

    private WalletItemDTO convertEntityToDto(WalletItem wi) {
        WalletItemDTO dto = new WalletItemDTO();
        dto.setId(wi.getId());
        dto.setWallet(wi.getWallet().getId());
        dto.setDate(wi.getDate());
        dto.setType(wi.getType().getValue());
        dto.setDescription(wi.getDescription());
        dto.setValue(wi.getValue());

        return dto;
    }

    private DateTimeFormatter getDateFormatter() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return formatter;
    }
}
