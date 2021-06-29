package com.wallet.controller;

import com.wallet.dto.WalletItemDTO;
import com.wallet.entity.UserWallet;
import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import com.wallet.response.Response;
import com.wallet.service.UserWalletService;
import com.wallet.service.WalletItemService;
import com.wallet.util.Util;
import com.wallet.util.enums.TypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("wallet-item")
public class WalletItemController {

    @Autowired
    WalletItemService service;

    @Autowired
    UserWalletService userWalletService;

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

    @GetMapping(value = "/{wallet}")
    public ResponseEntity<Response<Page<WalletItemDTO>>> findBetweenDates(@PathVariable("wallet") Long wallet,
        @RequestParam("startDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate,
        @RequestParam("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date endDate,
        @RequestParam(name = "page", defaultValue = "0") int page) {

        Response<Page<WalletItemDTO>> response = new Response<>();

        Optional<UserWallet> uw = userWalletService.findByUsersIdAndWalletId(Util.getAuthenticatedUserId(), wallet);
        if(!uw.isPresent()){
            response.getErrors().add("Você não tem acesso a essa carteira.");
            return ResponseEntity.badRequest().body(response);
        }

        Page<WalletItemDTO> items = service.findBetweenDays(wallet, startDate, endDate, page)
                                           .map(i -> convertEntityToDto(i));
        response.setData(items);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/type/{wallet}")
    public ResponseEntity<Response<List<WalletItemDTO>>> findByWalletIdAndType(@PathVariable("wallet") Long wallet,
        @RequestParam("type") String type) {
        Response<List<WalletItemDTO>> response = new Response<>();
        List<WalletItemDTO> items = service.findByWalletAndType(wallet, TypeEnum.getEnum(type))
                                           .stream().map(i -> convertEntityToDto(i))
                                           .collect(Collectors.toList());
        response.setData(items);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/total/{wallet}")
    public ResponseEntity<Response<BigDecimal>> sumByWalletId(@PathVariable("wallet") Long wallet){
        Response<BigDecimal> response = new Response<>();
        BigDecimal value = service.sumByWalletId(wallet);
        response.setData(value == null ? BigDecimal.ZERO : value);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<Response<WalletItemDTO>> update(@Validated @RequestBody WalletItemDTO dto,
        BindingResult result){
        Response<WalletItemDTO> response = new Response<>();

        Optional<WalletItem> opt = service.findById(dto.getId());

        if(!opt.isPresent()){
            result.addError(new ObjectError("WalletItem", "WalletItem não encontrado."));
        } else if(opt.get().getWallet().getId().compareTo(dto.getWallet()) != 0){
            result.addError(new ObjectError("WalletItemChange",
"Você não pode alterar a carteira."));
        }

        if(result.hasErrors()){
            result.getAllErrors()
                    .forEach(e -> response.getErrors().add(e.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        WalletItem wi = service.save(convertDtoToEntity(dto));
        response.setData(convertEntityToDto(wi));
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(value = "/{walletItemId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<String>> delete(@PathVariable("walletItemId") Long walletItemId) {
        Response<String> response = new Response<>();

        Optional<WalletItem> opt = service.findById(walletItemId);
        if(!opt.isPresent()){
            response.getErrors().add("WalletItem de id " + walletItemId + " não encontrada.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        service.deleteById(walletItemId);
        response.setData("WalletItem de id " + walletItemId + " apagada com sucesso.");
        return ResponseEntity.ok().body(response);
    }

    private WalletItem convertDtoToEntity(WalletItemDTO dto) {
        Wallet w = new Wallet();
        w.setId(dto.getWallet());

        WalletItem wi = new WalletItem(dto.getId(), w, dto.getDate(),
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
}
